<script>
  /**
   * @fileoverview This page displays notifications from beehives
   * @module Notification
   */
  import { onMount } from "svelte";
  import Button from "../../../components/Buttons/Button.svelte";
  import CircleButton from "../../../components/Buttons/CircleButton.svelte";
  import { fade, fly } from "svelte/transition";
  import message from "../stores/message";
  /*
  var stompClient = null;

  var socket = new SockJS('/ws');
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function(frame) {
    console.log(frame);
    stompClient.subscribe('/all/messages', function(result) {
      show(JSON.parse(result.body));
    });
  });*/

  let messages = [];

  message.setMessage("Upozornenia");

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
   * Sets notification to state 'read'
   * @param id id of the notification
   */
  const setRead = (id) => {
    messages.forEach((element) => {
      if (element.id === id) {
        element.seen = !element.seen;
        fetch("", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            type: "update",
            data: id,
            token: data.sessionid,
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
      //TODO rewrite remove
      // POST(
      //   "",
      //   JSON.stringify({
      //     type: "delete",
      //     data: id,
      //     token: data.sessionid,
      //   }),
      // ).then((data) => console.log(data));

      messages.splice(index, 1);
    }
    messages = [...messages];
  };

  /*
  let socket = new SockJS('/ws')
  let privateStompClient = Stomp.over(socket)
  privateStompClient.connect({}, function (frame) {
    console.log(frame)
    privateStompClient.subscribe('/user/specific', function (result) {
      console.log(result.body)
      console.log("message");
    //  show(JSON.parse(result.body))
    });
  })

  socket = new SockJS('/ws');
  let stompClient = Stomp.over(socket);
  stompClient.connect({}, function(frame) {
    console.log(frame);
    stompClient.subscribe('/all/messages', function(result) {
      
    });
    setTimeout(() => sendPrivateMessage(), 3000);
  });

  function sendPrivateMessage() {
    stompClient.send("/app/application", {},
      JSON.stringify({text:"hello", to:"test"}));
  }*/

  /**
   * Registers a service worker that tries to send continuous notifications even when page is closed
   */
  function registerServiceWorker() {
    console.log("register...");
    if ("serviceWorker" in navigator) {
      navigator.serviceWorker
        .register("../js/service-worker.js?token=" + getCookie("sessionid"))
        .then(function (registration) {
          console.log("Service worker successfully registered.");
          return registration;
        })
        .catch(function (err) {
          console.error("Unable to register service worker.", err);
        });
    } else console.log("Your browser does not support service workers.");
  }

  /**
   * Asks for permission to send notifications trough browser
   */
  function askPermission() {
    return new Promise(function (resolve, reject) {
      const permissionResult = Notification.requestPermission(
        function (result) {
          resolve(result);
        },
      );

      if (permissionResult) {
        permissionResult.then(resolve, reject);
      }
    }).then(function (permissionResult) {
      if (permissionResult !== "granted") {
        throw new Error("We weren't granted permission.");
      } else {
        // registerServiceWorker();
        //const notification = new Notification("Test", {body: "this is body", data: {}, icon: "/img/beeman.png"});
      }
    });
  }

  /**
   * Gets a cookie by name
   * @param name
   */
  function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(";").shift();
    return null;
  }

  askPermission();

  onMount(() => {
    registerServiceWorker();
  });

  function initialiseState() {
    // Are Notifications supported in the service worker?
    if (!("showNotification" in ServiceWorkerRegistration.prototype)) {
      console.warn("Notifications aren't supported.");
      return;
    }

    // Check the current Notification permission.
    // If its denied, it's a permanent block until the
    // user changes the permission
    if (Notification.permission === "denied") {
      console.warn("The user has blocked notifications.");
      return;
    }

    // Check if push messaging is supported
    if (!("PushManager" in window)) {
      console.warn("Push messaging isn't supported.");
      return;
    }

    // We need the service worker registration to check for a subscription
    navigator.serviceWorker.ready.then(function (serviceWorkerRegistration) {
      // Do we already have a push message subscription?
      serviceWorkerRegistration.pushManager
        .getSubscription()
        .then(function (subscription) {
          // Enable any UI which subscribes / unsubscribes from
          // push messages.

          if (!subscription) {
            // We aren't subscribed to push, so set UI
            // to allow the user to enable push
            return;
          }

          //TODO create method
          // Keep your server in sync with the latest subscriptionId
          sendSubscriptionToServer(subscription);

          // Set your UI to show they have subscribed for
          // push messages
        })
        .catch(function (err) {
          console.warn("Error during getSubscription()", err);
        });
    });

    self.addEventListener("push", function (event) {
      console.log("Received a push message", event);

      const title = "Yay a message.";
      const body = "We have received a push message.";
      const icon = "/images/icon-192x192.png";
      const tag = "simple-push-demo-notification-tag";

      event.waitUntil(
        self.registration.showNotification(title, {
          body: body,
          icon: icon,
          tag: tag,
        }),
      );
    });
  }
</script>

<div
  class="mx-auto mb-4 flex h-32 flex-col justify-between rounded-lg bg-white p-4 md:h-16 md:flex-row lg:w-5/6"
>
  <!-- title -->
  <h1 class="text-2xl font-semibold">Vaše upozornenia</h1>

  <Button
    text="Označiť všetky ako prečítané"
    onClick={() => {
      // TODO mato spravi request pre viac precitanych
      messages.forEach((element) => {
        setRead(element.id);
      });
    }}
  />
</div>

{#each messages as message, index}
  <div
    out:fly|local={{ x: 400, duration: 1000 }}
    class="  min-h-48 mx-auto mb-4 flex flex-col justify-between rounded-lg p-4 lg:w-5/6 {message.seen >
    0.5
      ? 'bg-slate-200'
      : 'bg-white'}"
  >
    <!-- title -->
    <div class="group flex flex-1 flex-col justify-center lg:justify-normal">
      <header class=" relative flex h-8 flex-row justify-between">
        <div class="flex w-48 items-center">
          <h1 class="mr-4 text-lg font-semibold">
            {message.timestamp}
          </h1>
        </div>
        <!-- <h1
                    class="inline group-hover:hidden
                opacity-100
                    duration-500
                    transition ease-in-out
                    group-hover:opacity-0"
                >
                    {new Date(message.timestamp * 1000).toLocaleString()}
                </h1> -->
        <div
          class="absolute right-0 top-0 flex gap-4 opacity-0
                    transition
                    duration-500 ease-in-out
                    group-hover:opacity-100
                    "
        >
          {#if !message.seen}
            <CircleButton
              image="envelope-check.svg"
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

{#if messages.length === 0}
  <div
    class="m-auto mt-4 flex h-24 flex-row items-center justify-center lg:w-5/6"
  >
    <div
      class="h-20 w-20 bg-[url('/icons/bell.svg')] bg-cover bg-no-repeat opacity-50"
    ></div>
  </div>
  <h1 class="my-4 text-center text-4xl font-bold text-slate-600">
    Zatiaľ žiadne upozornenia!
  </h1>
  <h1 class="my-4 text-center text-base text-slate-600">
    Pri novom upozornení dostanete správu emailom
  </h1>
{/if}
