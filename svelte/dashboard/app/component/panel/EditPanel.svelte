<script>
  import Logo from "../../../../components/Logo.svelte";

  import { onMount } from "svelte";
  import PanelRoot from "./PanelRoot.svelte";
  import * as cardUtils from "../cards/cardUtilities";
  import GridAdd from "../cards/utils/GridAdd.svelte";

  const gridReference = "dashboardGrid";

  const components = cardUtils.getCardTypes();
  let itemSideSize = 100;
  onMount(function () {
    let panelGrid = document.getElementById("panelGrid");

    itemSideSize = panelGrid.getBoundingClientRect().width;

    //TODO add resize event for itemSideSize
  });
</script>

<div class="box-border h-screen max-w-full overflow-y-scroll no-scrollbar">
  <Logo />
  <hr />
  <div
    id="panelGrid"
    class="box-border grid flex-1 gap-2 overflow-y-scroll px-2.5 no-scrollbar"
    style:--row-width={itemSideSize + "px"}
    style:--itemCount={components.length}
  >
    {#each components as item, index}
      <GridAdd
        gridItemComponent={item.component}
        {gridReference}
        gridItemProps={{
          cardStates: {
            mode: "add",
            title: item.format, // TODO zmenit na nejaky string z listu, aby bola mozna localizacia
            data: [{ type: "dummy", from: "all", to: "now" }], //default type of data in card item
          },
        }}
        className="grid place-items-center flex-1"
      >
        <svelte:component
          this={item.component}
          cardStates={{
            mode: "add",
            title: item.format, // TODO zmenit na nejaky string z listu, aby bola mozna localizacia
            data: [{ type: "dummy", from: "all", to: "now" }], //default type of data in card item
          }}
        />
      </GridAdd>
    {/each}
  </div>
</div>

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
