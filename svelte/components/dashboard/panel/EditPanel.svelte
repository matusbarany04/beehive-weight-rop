<script>
  import Logo from "../../Logo.svelte";

  import { onMount } from "svelte";
  import PanelRoot from "./PanelRoot.svelte";
  import * as cardUtils from "../cards/cardUtilities";
  export let onDragStart = () => {};
  export let onDragEnd = () => {};

  const components = cardUtils.getCardTypes();
  let itemSideSize = 100;
  onMount(function () {
    let panelGrid = document.getElementById("panelGrid");

    itemSideSize = panelGrid.getBoundingClientRect().width;

    //TODO add resize event for itemSideSize
  });
</script>

<PanelRoot>
  <Logo />
  <hr />
  <div
    id="panelGrid"
    style:--row-width={itemSideSize + "px"}
    style:--itemCount={components.length}
  >
    {#each components as item, index}
      <svelte:component
        this={item.component}
        cardStates={{
          x: 1,
          y: index + 1,
          spanX: 1,
          spanY: 1,
          mode: "add",
          title: item.format, // TODO zmenit na nejaky string z listu, aby bola mozna localizacia 
          data: [{ type: "dummy", from: "all", to: "now" }], //default type of data in card item
        }}
        {onDragStart}
        {onDragEnd}
      />
    {/each}
  </div>
</PanelRoot>

<style lang="scss">
  #panelGrid {
    width: 100%;
    box-sizing: border-box;
    padding: 0 10px;
    flex: 1;
    overflow-y: scroll;
    display: grid;
    gap: 10px;
    grid-template-rows: repeat(var(--itemCount), calc(var(--row-width) - 20px));

    grid-template-columns: repeat(1, calc(var(--row-width) - 20px));
  }

  .down {
    position: absolute;
    bottom: 0;
  }
</style>
