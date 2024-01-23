<script>
  /**
   * @fileoverview This page displays notifications from beehives
   * @module Notification
   */
  import Button from "../../../components/Buttons/Button.svelte";
  import CircleButton from "../../../components/Buttons/CircleButton.svelte";
  import { fade, fly } from "svelte/transition";
  import message from "../stores/message";
  import { getLanguageInstance } from "../../../components/language/languageRepository";

  const li = getLanguageInstance();

  Notification.requestPermission().then((permission) => {
    if (permission === "granted") {
      registerService();
    } else if (permission === "denied") {
      console.log("Notification permission denied.");
    } else if (permission === "default") {
      console.log("Notification permission prompt closed without a choice.");
    }
  });

  function registerService() {
    navigator.serviceWorker
      .register("../js/service-worker.js")
      .then(function (registration) {
        console.log("Service worker successfully registered.");

        registration.pushManager
          .subscribe({
            userVisibleOnly: true,
            applicationServerKey:
              "BCZCy-snf9UZVm6M74AoNGmmkuSqCs-sWcmCZLiiytyyA8ZCBMSLa3NXhE5AUFwGoqeqs8wCJoIzqcdCOZ6Z8LI",
          })
          .then(function (subscription) {
            console.log("Subscribed for push:", JSON.stringify(subscription));
            subscribeNotifications(subscription);
          })
          .catch(function (error) {
            console.log("Subscription failed:", error);
          });
      })
      .catch(function (error) {
        console.log("Service worker registration failed:", error);
      });
  }

  function subscribeNotifications(subscription) {
    fetch("/user/subscribe", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(subscription),
    });
  }

  let messages = null;

  message.setMessage(li.get("notifications.page"));

  fetch("/dashboardApi/getNotifications")
    .then((response) => {
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      return response.json();
    })
    .then((data) => {
      console.log(data);
      messages = data.notifications;
    })
    .catch((error) => {
      console.error(
        "There was a problem with the fetch operation:",
        error.message,
      );
    });

  /**
   * Sets notification to state 'read' inside a database
   * @param id id of the notification
   */
  const setRead = (id) => {
    messages.forEach((element) => {
      if (element.id === id) {
        element.seen = 1;
        // TODO we can also look if any were changed and not call fetch for nothing
        fetch("/dashboardApi/updateNotification", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            type: "update",
            id: id,
          }),
        })
          .then((response) => response.json())
          .then((data) => console.log(data))
          .catch((error) => console.error("Error:", error));
      }
    });
    //TODO post marked as read
    messages = [...messages];
  };

  /**
   * Deletes notification from database and removes it from page
   * @param id id of the notification
   */
  const remove = (id) => {
    const index = messages
      .map(function (e) {
        return e.id;
      })
      .indexOf(id);

    if (index > -1) {
      fetch(`/dashboardApi/deleteNotification?id=${id}`, {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
        },
      })
        .then((response) => response.json())
        .then((data) => console.log(data))
        .catch((error) => console.error("Error:", error));

      messages.splice(index, 1);
    }
    messages = [...messages];
  };

  /**
   * Gets a cookie by name from browser
   * @param name
   */
  function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(";").shift();
    return null;
  }
</script>

<svelte:head>
  <title>{li.get("notifications.page")}</title>
  <meta name="Analytika" content={li.get("notifications.about")} />
</svelte:head>
{#if messages}
  <div
    class="mx-auto mb-4 flex h-32 flex-col justify-between rounded-lg bg-white p-4 shadow shadow-tertiary-300 md:h-16 md:flex-row lg:w-5/6"
  >
    <!-- title -->
    <h1 class="text-2xl font-semibold">{li.get("notifications.title")}</h1>

    <Button
      text={li.get("notifications.btn_mark_read")}
      onClick={() => {
        // TODO mato spravi request pre viac precitanych
        messages.forEach((element) => {
          setRead(element.id);
        });
      }}
    />
  </div>

  {#each messages as message, index (message.id)}
    <div
      out:fly|local={{ x: 400, duration: 1000 }}
      class=" min-h-48 mx-auto mb-4 flex flex-col justify-between rounded-lg p-4 shadow shadow-tertiary-300 lg:w-5/6 {message.seen >
      0.5
        ? 'bg-slate-200'
        : 'bg-white'}"
      key={message.id}
    >
      <!-- title -->
      <div class="group flex flex-1 flex-col justify-center lg:justify-normal">
        <header class=" relative flex h-8 flex-row justify-between">
          <div class="flex items-center">
            <h1 class="mr-4 text-lg font-semibold">
              {message.title}
            </h1>
          </div>
          <h1
            class="inline opacity-100
                  transition
                      duration-500
                      ease-in-out group-hover:hidden
                      group-hover:opacity-0"
          >
            {new Date(message.timestamp).toLocaleString()}
          </h1>
          <div
            class="absolute right-0 top-0 flex gap-4 opacity-0
                    transition
                    duration-500 ease-in-out
                    group-hover:opacity-100
                    "
          >
            {#if !message.seen}
              <CircleButton
                image="icons/envelope-check.svg"
                type="secondary"
                onClick={() => {
                  setRead(message.id);
                }}
              />
            {/if}
            <CircleButton
              image="icons/delete.svg"
              type="error"
              onClick={() => {
                remove(message.id);
              }}
            />
          </div>
        </header>

        <h4 class="text-base font-normal">
          {message.message}
        </h4>
      </div>
    </div>
  {/each}
{:else}
  <div
    class="loading mx-auto mb-4 flex h-16 flex-col justify-between rounded-lg bg-tertiary-200 p-4 lg:w-5/6"
  ></div>
  {#each Array.from({ length: 3 }) as _, i}
    <div
      class="loading mx-auto mb-4 flex h-24 flex-col justify-between rounded-lg bg-tertiary-200 p-4 lg:w-5/6"
    ></div>
  {/each}
{/if}

{#if messages != null && messages.length === 0}
  <div
    class="m-auto mt-4 flex h-24 flex-row items-center justify-center lg:w-5/6"
  >
    <div
      class="h-20 w-20 bg-[url('/icons/bell.svg')] bg-cover bg-no-repeat opacity-50"
    ></div>
  </div>
  <!--  TODO translate -->
  <h1 class="my-4 text-center text-4xl font-bold text-slate-600">
    {li.get("notifications.no_notifications")}
  </h1>
  <h1 class="my-4 text-center text-base text-slate-600">
    {li.get("notifications.no_notifications_suggestion")}
  </h1>
{/if}

<style>
  .loading {
    animation: flash 3s infinite;
  }

  @keyframes flash {
    0%,
    100% {
      opacity: 0.5;
    }
    25%,
    75% {
      opacity: 1;
    }
  }
</style>
