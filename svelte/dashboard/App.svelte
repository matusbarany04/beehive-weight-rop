<script>
  import Router from "./app/routing/Router.svelte";
  import { prefix } from "../components/router/prefix";
  import shared from "./app/stores/shared";
  import Loading from "../components/pages/Loading.svelte";
  import { SvelteToast } from "@zerodevx/svelte-toast";
  import "../components/Toast/toastStyles.css";
  import settingsLoader from "./app/stores/settingsLoader";
  import {
    initLanguage,
    setLanguageDataLoadedEvent,
  } from "../components/language/languageRepository";
  // Initiate all fetch operations in an async function
  async function loadData() {
    //sets prefix for all route links

    prefix.setPrefix("/dashboard");
    // fetches all dashboard data to dataHandler store
    shared.fetchUser();
    shared.fetchBeehives();
    shared.fetchStatuses();
    settingsLoader.loadSettings();
    // fetches all shared data between all components and saves them to corresponding stores
  }

  loadData();

  let languageLoaded = false;
  setLanguageDataLoadedEvent((json) => {
    languageLoaded = true;
    console.log("language loaded", json);
  });

  initLanguage("dashboard");
</script>

<SvelteToast />
<div class="bg-slate-300">
  <!-- Uncomment these if you want to include them -->
  <!-- <Sidenav class="sidenav" />
  <Panel /> -->
  {#if languageLoaded}
    <Router />
  {:else}
    <div class="grid h-screen w-full place-items-center bg-primary-100">
      <p class="text-slate-50">loading language pack...</p>
    </div>
  {/if}
</div>

<style>
  @import url("https://fonts.googleapis.com/css2?family=Bree+Serif&display=swap");
  @import url("https://fonts.googleapis.com/css2?family=Jost&display=swap");
  @import url("https://fonts.googleapis.com/css2?family=Inter:wght@100;200;300;400;500;600;700;800;900&family=Open+Sans:wght@300;400;600;700&display=swap");
  @import url("https://fonts.googleapis.com/css2?family=Exo+2:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap");
  /*  futuristicke */

  @import url("https://fonts.googleapis.com/css2?family=Rubik:ital,wght@0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap");

  @import "tailwindcss/base";
  @import "tailwindcss/components";
  @import "tailwindcss/utilities";

  .shadow {
    box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
  }

  ::-webkit-scrollbar {
    width: 5px;
    height: 7px;
  }

  ::-webkit-scrollbar-thumb {
    background-color: theme("colors.primary.100");
    outline: 1px solid #1e202a;
    border-radius: 50px;
  }

  ::-webkit-scrollbar-track {
    box-shadow: inset 0 0 4px rgba(0, 0, 0, 0.2);
  }

  @-moz-document url-prefix() {
    .scroller {
      scrollbar-width: thin;
      scrollbar-color: #1e202a;
    }
  }
</style>
