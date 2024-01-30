<script>
  /**
   * @fileoverview This page shows graphs and charts related to beehives and their data
   * @module HomePage
   */
  import { onMount, setContext, tick } from "svelte";

  import Button from "../../../components/Buttons/Button.svelte";
  import EditPanel from "../component/panel/EditPanel.svelte";
  import * as cardUtils from "../component/cards/cardUtilities";
  import staticFuncs, {
    generateUUID,
    TW_BREAKPOINTS,
  } from "../../../components/lib/utils/staticFuncs";
  import shared, { onLoad } from "../stores/shared";
  import Loading from "../../../components/pages/Loading.svelte";
  import message from "../stores/message";
  import Grid from "../component/cards/utils/Grid.svelte";
  import GridItem from "../component/cards/utils/GridItem.svelte";
  import panelState from "../component/panel/panelState";
  import PercentageCard from "../component/cards/PercentageCard.svelte";
  import WeatherCard from "../component/cards/WeatherCard.svelte";
  import MapCard from "../component/cards/MapCard.svelte";
  import { getCardByFormat } from "../component/cards/cardUtilities";
  import toast from "../../../components/Toast/toast";
  import Login from "../../../general/pages/Login.svelte";
  import { getLanguageInstance } from "../../../components/language/languageRepository";
  import sf from "../../../components/lib/utils/staticFuncs";
  import SimpleDialog from "../../../components/SimpleDialog.svelte";

  let cardList = [];

  let editMode = false;
  let editButton = true;
  let renderCards = false;
  let stacked = false;
  let grid;

  const li = getLanguageInstance();

  let showDialog = false;

  function checkDialog() {
    showDialog = editMode && !editButton;
  }

  let resizeWindowEvent = (event) => {
    checkDialog();
    if (TW_BREAKPOINTS.md > window.innerWidth) {
      if (!stacked) {
        stacked = true;
        refreshDashboard();
      }
      if (editMode) {
        refreshDashboard();
      }
      // editMode = false;

      editButton = false;
    } else {
      editButton = true;
      if (stacked) {
        stacked = false;
        refreshDashboard();
      }
    }
  };

  let user;
  message.setMessage(sf.formatString(li.get("home.greeting_noon"), ""));
  onLoad(["user"], (userObj) => {
    user = userObj;
    message.setMessage(
      sf.formatString(li.get("home.greeting_noon"), user.name),
    );
    cardList = JSON.parse(user.dashboardData);
    // console.log(user);
    // renderCards = true;
  });

  onMount(function () {
    resizeWindowEvent();
  });

  onLoad(["beehives", "statuses", "user"], (beehives, statuses, user) => {
    renderCards = true;
  });

  async function save(data) {
    try {
      let response = await fetch("/user/saveDashboard", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ data: JSON.stringify(data) }),
      });
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }

      let output = await response.json();
      await shared.fetchUser();

      toast.push("New layout saved!");
      return output;
    } catch (error) {
      console.error("There was a problem with the fetch operation:", error);
      throw error;
    }
  }

  let refreshDashboard = () => {
    if (renderCards) {
      renderCards = false;
      tick().then(() => {
        renderCards = true;
      });
    }
  };

  let restoreLayout = () => {
    cardList = JSON.parse(user.dashboardData);
    refreshDashboard();
    editMode = false;
    panelState.resetMode();
  };

  setContext("dashboard", {
    saveGrid: () => {
      save(grid.serialize());
      console.log("serialized grid", grid.serialize());
      cardList = grid.serialize();
      refreshDashboard();
    },
  });
</script>

<svelte:window on:resize={resizeWindowEvent} />

<svelte:head>
  <title>{li.get("home.page")}</title>
  <meta name="description" content="Dashboard" />
</svelte:head>

{#if editButton}
  <div class="absolute right-0 top-0 z-50 flex w-min justify-end gap-3 p-4">
    <div class="flex gap-4">
      {#if editMode}
        <Button
          text={li.get("home.cancel_changes")}
          type="secondary"
          onClick={restoreLayout}
        />
      {/if}
      <!-- TODO spravit len disabled mozno v buducnosti, 
                                          pridat aj popup preco to zmizlo (ked sa zmensi sirka okna) -->
      <Button
        text={!editMode ? li.get("home.edit") : li.get("home.save")}
        type={!editMode ? "primary" : "confirm"}
        onClick={() => {
          console.log("clicked on edit or save button!!!");
          if (editMode) {
            save(grid.serialize());
            panelState.resetMode();
          } else {
            panelState.setMode("dashboardEdit");
          }

          editMode = !editMode;
        }}
      />
    </div>
  </div>
{/if}

<div class="flex min-h-screen w-full flex-1 flex-col">
  {#if renderCards}
    <Grid
      bind:this={grid}
      referenceName="dashboardGrid"
      draggable={editMode}
      xCount={4}
      yCount={4}
      padding={10}
      {stacked}
      className="w-full aspect-square"
      items={cardList.map((card) => ({
        ...card,
        component: getCardByFormat(card.component),
      }))}
    ></Grid>
    <div class="h-16 w-full"></div>
  {:else}
    <!--    <Loading />-->
    <div class="grid aspect-square w-full grid-cols-4 grid-rows-4 gap-4">
      {#each Array.from({ length: 16 }) as _, i}
        <div class="loading rounded-md bg-tertiary-200"></div>
      {/each}
    </div>
  {/if}
</div>
<!-- TODO add translation -->
<SimpleDialog
  bind:show={showDialog}
  dismissible={false}
  dismissAction={() => {
    refreshDashboard();
    editMode = false;
  }}
  action={() => {
    save(grid.serialize());
    editMode = false;
  }}
  message={`Aby ste mohli nadaľej pokračovať v úprave, rozšírte okno prehliadača, nakoľko toto rozlíšenie nie je podporované`}
  positiveButton="Uložiť"
  negativeButton="Zrušiť zmeny"
/>

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
