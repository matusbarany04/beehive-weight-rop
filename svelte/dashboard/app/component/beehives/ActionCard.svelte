<script>
  import { getLanguageInstance } from "../../../../components/language/languageRepository";
  import CircleButton from "../../../../components/Buttons/CircleButton.svelte";
  import { fade, fly } from "svelte/transition";
  import { generateUUID } from "../../../../components/lib/utils/staticFuncs";

  export let className;

  export let actionObject;

  export let loading = false;

  export let failed = false;

  export let finished = false;

  const li = getLanguageInstance();

  let timeLeft = 0;

  const updateTime = () => {
    timeLeft = new Date(actionObject.executionTime - new Date().getTime());
    setTimeout(() => {
      updateTime();
    }, 1000);
  };
  updateTime();

  function formatTime(milliseconds) {
    if (milliseconds < 0) {
      return "Akcia sa každú chvíľu vykoná";
    }

    // Convert milliseconds to seconds
    const seconds = Math.floor(milliseconds / 1000);

    if (seconds < 60) {
      return `${seconds} second${seconds !== 1 ? "s" : ""}`;
    }

    const minutes = Math.floor(seconds / 60);
    if (minutes < 60) {
      return `${minutes} minute${minutes !== 1 ? "s" : ""}`;
    }

    const hours = Math.floor(minutes / 60);
    return `${hours} hour${hours !== 1 ? "s" : ""}`;
  }

  export let onDeleteCard = () => {
    console.warn("on delete not implemented!");
  };
</script>

<div
  id={generateUUID()}
  out:fly|local={{ x: 400, duration: 1000 }}
  class="{className} min-h-16 relative relative mx-auto mb-4 flex flex-col rounded-lg bg-white p-4 shadow shadow-tertiary-300 lg:w-5/6"
>
  <!-- title -->
  <div>
    <h1 class="inline text-2xl font-semibold">
      {li.get(`actions.${actionObject.type}`) || actionObject.type}
    </h1>
    <h1 class="mx-2 inline font-bold">-</h1>
    <h1 class="inline text-lg">
      {li.get(`statuses.${actionObject.status}`)}
    </h1>
  </div>

  <h1 class="text-base font-normal">
    {actionObject.description ?? "Akcia nemá žiaden popis"}
  </h1>

  {#if actionObject.status === "PENDING"}
    <div class="absolute right-2 top-2">
      <CircleButton image="icons/trash.svg" type="error" onClick={onDeleteCard}
      ></CircleButton>
    </div>
  {/if}

  <div class="block flex-1">
    <p>Parametre</p>
    {#each Object.entries(JSON.parse(actionObject.params)) as [paramKey, paramValue]}
      <div>
        <strong>{paramKey}:</strong>
        {JSON.stringify(paramValue)}
      </div>
    {/each}
  </div>

  <p class="text-end">scheduled in {formatTime(timeLeft.getTime())}</p>

  {#if loading}
    <div class="absolute bottom-0 left-0 h-2 w-full">
      <div
        class="loadingBar bottom-0 left-0 top-0 rounded-3xl bg-secondary-400"
      ></div>
    </div>
  {/if}

  {#if finished || failed}
    <div
      class="absolute bottom-0 left-0 h-2 w-full rounded
      {finished ? 'bg-confirm-600' : ''} 
      {failed ? 'bg-error-800' : ''}"
    ></div>
  {/if}
</div>

<style>
  .loadingBar {
    position: absolute;
    animation: borealisBar 2s linear infinite;
  }

  @keyframes borealisBar {
    0% {
      left: 0%;
      right: 100%;
      width: 0%;
    }
    10% {
      left: 0%;
      right: 75%;
      width: 25%;
    }
    90% {
      right: 0%;
      left: 75%;
      width: 25%;
    }
    100% {
      left: 100%;
      right: 0%;
      width: 0%;
    }
  }
</style>
