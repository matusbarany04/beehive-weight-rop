<script>
  import { onMount } from "svelte";
  import Button from "../../../components/Buttons/Button.svelte";
  import CircleButton from "../../../components/Buttons/CircleButton.svelte";
  import { fade, fly } from "svelte/transition";
  import {dataHandler} from "../../../components/dashboard/cards/dataHandler";


  let messages = [];

  fetch("/dashboardApi/getNotifications")
    .then(response => {
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      return response.json();
    })
    .then(data => {
      console.log(data);
      messages = data.notifications;
      
    })
    .catch(error => {
      console.error('There was a problem with the fetch operation:', error.message);
    });

  const setRead = (id) => {
    messages.forEach((element) => {
      if (element.id === id) {
        element.seen = !element.seen;
        fetch("", {
          method: "POST",
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify({
            type: "update",
            data: id,
            token: data.sessionid,
          })
        })
          .then(response => response.json())
          .then(data => console.log(data))
          .catch(error => console.error('Error:', error));

        // .then((res) => res.json())
      }
    });
    //TODO post marked as read
    messages = [...messages];
  };

  const remove = (id) => {
    var index = messages
      .map(function (e) {
        return e.id;
      })
      .indexOf(id);

    if (index > -1) {
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
</script>

<div
  class="mb-4 p-4 h-32 mx-auto flex flex-col justify-between bg-white rounded-lg lg:w-5/6 md:h-16 md:flex-row"
>
  <!-- title -->
  <h1 class="text-2xl font-semibold">Vaše upozornenia</h1>

  <Button
    text="Označiť všetky ako prečítané"
    onClick={() => {
      // mato spravi request pre viac precitanych
      messages.forEach((element) => {
        setRead(element.id);
      });
    }}
  />
</div>

{#each messages as message, index}
  <div
    out:fly|local={{ x: 400, duration: 1000 }}
    class="  mb-4 p-4 mx-auto min-h-48 flex justify-between rounded-lg lg:w-5/6 flex-col {message.seen >
    0.5
      ? 'bg-slate-200'
      : 'bg-white'}"
  >
    <!-- title -->
    <div class="group flex-1 flex flex-col justify-center lg:justify-normal">
      <header class=" flex flex-row justify-between h-8 relative">
        <div class="w-48 flex items-center">
          <h1 class="text-lg font-semibold mr-4">
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
          class="absolute top-0 right-0 gap-4 flex opacity-0
                    duration-500
                    transition ease-in-out
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
    class="mt-4 h-24 lg:w-5/6 m-auto flex flex-row items-center justify-center"
  >
    <div
      class="h-20 w-20 bg-[url('/icons/bell.svg')] bg-no-repeat bg-cover opacity-50"
    />
  </div>
  <h1 class="text-center my-4 text-slate-600 text-4xl font-bold">
    Zatiaľ žiadne upozornenia!
  </h1>
  <h1 class="text-center my-4 text-slate-600 text-base">
    Pri novom upozornení dostanete správu emailom
  </h1>
{/if}
