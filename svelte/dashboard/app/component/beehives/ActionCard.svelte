<script>
  import { getLanguageInstance } from "../../../../components/language/languageRepository";

  export let className;

  export let actionObject;

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
</script>

<div
  class="{className} min-h-16 relative mx-auto mb-4 flex flex-col rounded-lg bg-white p-4 lg:w-5/6"
>
  <!-- title -->
  <h1 class="text-2xl font-semibold">
    {li.get(`actions.${actionObject.type}`)}
  </h1>
  <h1 class="text-base font-normal">
    {actionObject.description ?? "Akcia nemá žiaden popis"}
  </h1>

  <h1 class="absolute right-2 top-2 text-lg">
    {actionObject.status}
  </h1>

  <div class="block flex-1">
    <p>Parametre</p>
    {#each Object.entries(JSON.parse(actionObject.params)) as [paramKey, paramValue]}
      <div>
        <strong>{paramKey}:</strong>
        {JSON.stringify(paramValue)}
      </div>
    {/each}
  </div>

  <p class="text-end">{formatTime(timeLeft.getTime())}</p>
</div>
