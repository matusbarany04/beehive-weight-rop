<script context="module">
  import { getContext } from "svelte";

  const GRID_CONTEXT_NAME = Symbol("grid-context");

  export function getGridContext() {
    let context = getContext(GRID_CONTEXT_NAME);
    if (!context) {
      throw new Error(
        `<GridItem /> is missing a parent <Grid /> component. Make sure you are using the component inside a <Grid />.`,
      );
    }
    return context;
  }
</script>

<script>
  import { setContext } from "svelte";
  import { onMount } from "svelte";
  import GridItem from "./GridItem.svelte";
  import { Item } from "./item";
  import Shadow from "./Shadow.svelte";

  export let className = "";
  export let xCount = 1;
  export let yCount = 1;

  export let draggable = false;

  export let padding = 100;

  let rootElement;

  let width = 0;
  let height = 0;

  let resizeObserver;

  /**
   * @type array<Item>
   */
  let gridItemsRefs = [];
  const itemUtils = {
    /**
     * @param id {string}
     */
    getItemById: function (id) {
      for (/** @type {Item} */ const item of gridItemsRefs) {
        if (item.id === id) return item;
      }
      console.error(`Item with id ${id} doesn't exist!!`);
      return null;
    },
    updateItemDraggability() {
      for (/** @type {Item} */ const item of gridItemsRefs) {
        item.draggable = draggable;
      }
    },
  };

  $: {
    draggable;
    itemUtils.updateItemDraggability();
  }

  let itemWidth = width / xCount;
  const itemWidthFunctions = {
    getItemWidth() {
      return itemWidth;
    },
    setWidth(value) {
      itemWidth = value;
    },
    updateWidth() {
      itemWidth = (width - padding * Math.max(0, xCount - 1)) / xCount;
    },
  };

  let shadowItemRef = null;
  let shadowItem = null;
  const utilityFunctions = {
    getRootElementRef() {
      return rootElement;
    },
    itemWidth: { ...itemWidthFunctions },
    subscribeItem(gridItem) {
      gridItemsRefs.push(gridItem);
      gridItem.draggable = draggable;
    },
    /**
     *
     * @param item {Item}
     * @param mousePosition {Object} relative to inside of the item
     */
    requestNewPosition(item, mousePosition) {
      let coords = pointAsCoordinates(
        item.xCoordinate + mousePosition.x,
        item.yCoordinate + mousePosition.y,
      );

      item.x = coords.x;
      item.y = coords.y;
      positionGridItem(item);
    },
    setShadow(itemRef) {
      shadowItemRef = itemRef;
      shadowItem = new Item(itemRef.x, itemRef.y, itemRef.w, itemRef.h);
      positionGridItem(shadowItem);
    },
    updateShadow(itemX, itemY) {
      let coords = pointAsCoordinates(itemX, itemY);
      shadowItem.x = coords.x;
      shadowItem.y = coords.y;
      positionGridItem(shadowItem);
    },
    getShadowPos() {
      return { x: shadowItem.x, y: shadowItem.y };
    },
    resetShadow() {
      shadowItem = null;
      shadowItemRef = null;
    },
  };

  setContext(GRID_CONTEXT_NAME, utilityFunctions);

  onMount(() => {
    resizeObserver = new ResizeObserver((entries) => {
      const rect = entries[0].contentRect;
      width = rect.width;
      height = rect.height;
      itemWidthFunctions.updateWidth();

      gridItemsRefs.forEach((/** @type {Item} */ gridItem) => {
        positionGridItem(gridItem);
        gridItem.mounted = true;
      });
    });

    resizeObserver.observe(rootElement);

    // Cleanup observer on component destroy
    return () => {
      resizeObserver.disconnect();
    };
  });

  /**
   * Functions for positioning grid item might be a separate file in the future
   * Keep interpolation at minimum
   */

  function positionGridItem(gridItem) {
    gridItem.unitSize = itemWidthFunctions.getItemWidth();
    gridItem.xCoordinate =
      itemWidthFunctions.getItemWidth() * gridItem.x +
      padding * Math.max(0, gridItem.x);

    gridItem.yCoordinate =
      itemWidthFunctions.getItemWidth() * gridItem.y +
      padding * Math.max(0, gridItem.y);
  }

  function pointAsCoordinates(x, y) {
    let xCoord = Math.floor(x / (width / xCount));
    let yCoord = Math.floor(y / (height / yCount));
    return { x: xCoord, y: yCoord };
  }

  export let collidesWith = (item1, item2) => {
    return (
      item1.x <= item2.x + (item2.spanX - 1) &&
      item1.x + (item1.spanX - 1) >= item2.x &&
      item1.y <= item2.y + (item2.spanY - 1) &&
      item1.spanY - 1 + item1.y >= item2.y
    );
  };

  let checkCollision = (node) => {
    for (let i = 0; i < cardList.length; i++) {
      let element = cardList[i];

      if (element.id !== node.id) {
        if (collidesWith(node, element)) {
          return true;
        }
      }
    }
    return false;
  };
</script>

<div bind:this={rootElement} class="relative {className}">
  {#if shadowItem}
    <Shadow
      xCoordinate={shadowItem.xCoordinate}
      yCoordinate={shadowItem.yCoordinate}
      pixelWidth={shadowItem.pixelWidth}
      pixelHeight={shadowItem.pixelHeight}
      className="bg-primary-200"
    ></Shadow>
  {/if}
  <slot />
</div>
