<script>
  import { onMount, onDestroy } from "svelte";
  import { GridManager } from "./gridManager";

  /** @type {string} */
  export let gridReference;

  /**
   * @type {SvelteComponent}
   */
  export let gridItemComponent;

  /** @type {object} */
  export let gridItemProps;

  /** @type {string} */
  export let className;

  let gridAddItem;

  onMount(() => {
    let grid = GridManager.getGridById(gridReference);

    function handleMouseDown(event) {
      grid.newGridItem(gridItemComponent, gridItemProps);
    }

    gridAddItem.addEventListener("mousedown", handleMouseDown);

    // The return function inside onMount acts as a "cleanup" function,
    // which will run when the component is being destroyed.
    return () => {
      gridAddItem.removeEventListener("mousedown", handleMouseDown);
    };
  });
</script>

<div bind:this={gridAddItem} class={className}>
  <slot />
</div>
