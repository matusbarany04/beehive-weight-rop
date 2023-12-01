<script>
  import Router from "./app/routing/Router.svelte";
  import {
    setLanguageDataLoadedEvent,
    isValidLanguage,
    initLanguage,
  } from "../components/language/languageRepository";
  import Loading from "../components/pages/Loading.svelte";

  let languageLoaded = false;
  setLanguageDataLoadedEvent((json) => {
    languageLoaded = true;
    console.log("language loaded", json);
  });

  initLanguage("general");
</script>

<div class="min-h-screen w-screen">
  {#if languageLoaded}
    <Router />
  {:else}
    <div class="grid h-screen w-full place-items-center bg-primary-100">
      <p class="text-slate-50">loading language pack...</p>
    </div>
  {/if}
</div>

<style>
  @import "tailwindcss/base";
  @import "tailwindcss/components";
  @import "tailwindcss/utilities";
</style>
