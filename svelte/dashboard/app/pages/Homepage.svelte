<script>
  /**
   * @fileoverview This page shows graphs and charts related to beehives and their data
   * @module HomePage
   */
  import { onMount, setContext, tick } from "svelte";

  import Button from "../../../components/Buttons/Button.svelte";
  import EditPanel from "../component/panel/EditPanel.svelte";
  import * as cardUtils from "../component/cards/cardUtilities";
  import { generateUUID } from "../../../components/lib/utils/staticFuncs";
  import shared, { onLoad } from "../stores/shared";
  import Loading from "../../../components/pages/Loading.svelte";
  import message from "../stores/message";
  import Grid from "../component/cards/utils/Grid.svelte";
  import GridItem from "../component/cards/utils/GridItem.svelte";
  import panelState from "../component/panel/panelState";

  let cardList = [];

  let editMode = false;
  let smallScreen = false;
  let editButton = true;
  let renderCards = false;

  let user;
  message.setMessage("Dobrý deň");
  onLoad(["user"], (userObj) => {
    user = userObj;
    message.setMessage("Dobrý deň, včelár " + user.name);
    cardList = JSON.parse(user["dashboardData"]);
    console.log(cardList);
    renderCards = true;
  });

  onMount(function () {});

  onLoad(["beehives", "statuses"], (beehives, statuses) => {
    console.log("beehives, statuses", statuses, beehives);

    renderCards = true;
  });
</script>

<svelte:head>
  <title>DashBoard</title>
  <meta name="description" content="Svelte demo app" />
</svelte:head>

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
            // dashboardEditor.saveCardList();
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

<div class="flex min-h-screen w-full flex-1 flex-col">
  {#if renderCards}
    <Grid
      referenceName="dashboardGrid"
      draggable={editMode}
      xCount={4}
      yCount={4}
      padding={10}
      className="w-full aspect-square"
    >
      <!--{#each Array(2) as _, x}-->
      <!--  {#each Array(2) as _, y}-->
      <!--    <GridItem x={x * 2} {y} w={2} className="outline-primary-100 outline"-->
      <!--    ></GridItem>-->
      <!--  {/each}-->
      <!--{/each}-->

      <!--{#each cardList as item, i (item.id)}-->
      <!--  <GridItem x={item.x - 1} y={item.y - 1} w={item.spanX} h={item.spanY}>-->
      <!--    &lt;!&ndash;            this={cardUtils.getCardByFormat(item.component)}&ndash;&gt;-->
      <!--    <svelte:component-->
      <!--      this={cardUtils.getDeletedCard()}-->
      <!--      cardStates={{-->
      <!--        id: item.id,-->
      <!--        editing: editMode,-->
      <!--        title: item.title,-->
      <!--        data: [],-->
      <!--      }}-->
      <!--    />-->
      <!--  </GridItem>-->
      <!--{/each}-->
    </Grid>
    <div class="h-16 w-full"></div>
  {:else}
    <Loading />
  {/if}
</div>
