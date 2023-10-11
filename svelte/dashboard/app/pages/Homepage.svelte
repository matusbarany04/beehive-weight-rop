<script>
  /**
   * @fileoverview This page shows graphs and charts related to beehives and their data
   * @module HomePage
   */
  import { onMount, setContext, tick } from "svelte";

  import Button from "../../../components/Buttons/Button.svelte";
  import EditPanel from "../component/panel/EditPanel.svelte";
  import * as cardUtils from "../component/cards/cardUtilities";
  import {
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

  let cardList = [];

  let editMode = false;
  let editButton = true;
  let renderCards = false;
  let grid;

  let resizeWindowEvent = (event) => {
    if (TW_BREAKPOINTS.md > window.innerWidth) {
      if (editMode) {
        renderCards = false;
        tick().then(() => {
          renderCards = true;
        });
      }
      editMode = false;
      editButton = false;
    } else {
      editButton = true;
    }
  };

  let user;
  message.setMessage("Dobrý deň");
  onLoad(["user"], (userObj) => {
    user = userObj;
    message.setMessage("Dobrý deň, včelár " + user.name);
    cardList = JSON.parse(user.dashboardData);
    renderCards = true;
  });

  onMount(function () {
    onLoad(["beehives", "statuses"], (beehives, statuses) => {
      console.log("serialize, ", grid.serialize());
    });

    resizeWindowEvent();
  });

  onLoad(["beehives", "statuses"], (beehives, statuses) => {
    renderCards = true;
  });

  async function save(data) {
    console.log("data", data);
    try {
      let response = await fetch("/user/saveDashboard", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ data: JSON.stringify(data) }),
      });
      console.log(response);
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }

      let output = await response.json();
      return output;
    } catch (error) {
      console.error("There was a problem with the fetch operation:", error);
      throw error;
    }
  }
</script>

<svelte:window on:resize={resizeWindowEvent} />

<svelte:head>
  <title>DashBoard</title>
  <meta name="description" content="Dashboard" />
</svelte:head>

{#if editButton}
  <div class="absolute right-0 top-0 z-50 flex w-min justify-end gap-3 p-4">
    {#if editButton}
      <div class="flex gap-4">
        {#if editMode}
          <Button text="Zahodiť zmeny" type="secondary" />
        {/if}
        <!-- TODO spravit len disabled mozno v buducnosti, 
                                          pridat aj popup preco to zmizlo (ked sa zmensi sirka okna) -->
        <Button
          text={!editMode ? "upraviť" : "uložiť"}
          type={!editMode ? "primary" : "confirm"}
          onClick={() => {
            if (editMode) {
              console.log("saving this ... ", grid.serialize());
              save(grid.serialize());
              panelState.resetMode();
            } else {
              panelState.setMode("dashboardEdit");
            }

            editMode = !editMode;
          }}
        />
      </div>
    {/if}
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
      className="w-full aspect-square"
      items={cardList.map((card) => ({
        ...card,
        component: getCardByFormat(card.component),
      }))}
    ></Grid>
    <div class="h-16 w-full"></div>
  {:else}
    <Loading />
  {/if}
</div>
