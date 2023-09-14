<script>
  import Logo from "../../Logo.svelte";

  import {onMount} from "svelte";
  import PanelRoot from "./PanelRoot.svelte";
  import * as cardUtils from "../cards/cardUtilities";

  export let onDragStart = () => {
  };
  export let onDragEnd = () => {
  };

  const components = cardUtils.getCardTypes();
  let itemSideSize = 100;
  onMount(function () {
    let panelGrid = document.getElementById("panelGrid");

    itemSideSize = panelGrid.getBoundingClientRect().width;

    //TODO add resize event for itemSideSize
  });
</script>

<PanelRoot>
  <div class="max-w-full overflow-y-scroll h-screen box-border">
    <Logo/>
    <hr/>
    <div
      id="panelGrid"
      class="box-border overflow-y-scroll grid gap-2 flex-1 px-2.5"
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
  </div>

</PanelRoot>

<style lang="scss">
  #panelGrid {
 
    grid-template-rows: repeat(var(--itemCount), calc(var(--row-width) - 20px));
    grid-template-columns: repeat(1, calc(var(--row-width) - 20px));
  }

  .down {
    position: absolute;
    bottom: 0;
  }
</style>
