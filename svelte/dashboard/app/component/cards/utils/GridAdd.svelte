<script>
  import {onMount, onDestroy} from 'svelte';
  import {GridManager} from "./gridManager";

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

  // let gridRoot = getGridContext()

  let gridAddItem;

  onMount(() => {
    let grid = GridManager.getGridById(gridReference);

    function handleMouseDown(event) {
      console.log('Mouse down:', event);
      // component, props, x = 0, y = 0
      grid.newGridItem(gridItemComponent, gridItemProps);
    }

    gridAddItem.addEventListener('mousedown', handleMouseDown);

    // The return function inside onMount acts as a "cleanup" function, 
    // which will run when the component is being destroyed.
    return () => {
      gridAddItem.removeEventListener('mousedown', handleMouseDown);
    };
  });

</script>

<div bind:this={gridAddItem} class="{className}">
  <slot></slot>
</div>