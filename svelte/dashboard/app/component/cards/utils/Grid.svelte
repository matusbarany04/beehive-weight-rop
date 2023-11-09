<script context="module">
  import {getContext} from "svelte";

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
  import {setContext} from "svelte";
  import {onMount} from "svelte";
  import GridItem from "./GridItem.svelte";
  import {Item} from "./item";
  import Shadow from "./Shadow.svelte";
  import {Grid} from "./grid";
  import {GridManager} from "./gridManager";
  import {GridResolver} from "./gridResolver";

  /*
    Known possible bugs 
    Padding is static and grid will break when changed dynamically
   */
  export let className = "";
  export let xCount = 1;
  export let yCount = 1;

  export let draggable = false;

  export let padding = 100;

  export let items;

  export let referenceName;

  let rootElement;

  let width = 0;
  let height = 0;

  let resizeObserver;

  let grid = new Grid(width, height, xCount, yCount);

  // setting reference name to grid object
  if (referenceName != null) grid.id = referenceName;
  // registering grid to a static array
  GridManager.registerGrid(grid);

  $: {
    draggable;
    grid.updateItemDraggability(draggable);
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
    // BUG not really deleting, will fix later
    deleteGridItem(item) {
      for (let i = 0; i < grid.newItems.length; i++) {
        let it = grid.newItems[i];

        if (it.id === item.id) {
          grid.newItems.splice(i, 1);
          grid.newItems = [...grid.newItems];
        }
      }

      delete saveDataFunctions[item.id];

      let index = grid.gridItemRefs.indexOf(item);

      if (index > -1) {
        grid.gridItemRefs.splice(index, 1);
      }

      grid.gridItemRefs = [...grid.gridItemRefs];

      refreshItems();
    },
    getRootElementRef() {
      return rootElement;
    },
    getGridObject() {
      return grid;
    },
    itemWidth: {...itemWidthFunctions},
    subscribeItem(gridItem) {
      GridResolver.printGrid(xCount, yCount, grid.gridItemRefs);
      GridResolver.resolveAroundItem(
        xCount,
        yCount,
        grid.gridItemRefs,
        gridItem,
      );
      grid.gridItemRefs.push(gridItem);
      gridItem.draggable = draggable;

      GridResolver.printGrid(xCount, yCount, grid.gridItemRefs);
      refreshItems();
    },
    /**
     * @return {number}
     */
    getPadding() {
      return padding;
    },
    /**
     *
     * @param item {Item}
     * @param mousePosition {Object} relative to inside of the item
     */
    requestNewPosition(item, mousePosition = null) {
      if (mousePosition != null) {
        let collapsed = collapseMousePosition(item, mousePosition);

        let coords = pointAsCoordinates(collapsed.x, collapsed.y);
        
        // Min, max is for the item to not overflow out of the grid boundaries
        coords.x = Math.max(0,Math.min(coords.x, xCount - item.w));
        coords.y = Math.max(0,Math.min(coords.y, yCount - item.h));
        
        if (
          GridResolver.isPossible(xCount, yCount, [
            ...grid.gridItemRefs.filter((it) => it !== item),
            {...item, _x: coords.x, _y: coords.y},
          ])
        ) {
          item.x = coords.x;
          item.y = coords.y;
        }
      }
      positionGridItem(item);
    },
    requestNewResize(item, width, height) {
      if (
        GridResolver.isPossible(xCount, yCount, [
          ...grid.gridItemRefs.filter((it) => it !== item),
          {...item, _w: width, _h: height},
        ])
      ) {
        item.wh = {w: width, h: height};
      }
      positionGridItem(item);
    },
    setShadow(itemRef) {
      shadowItemRef = itemRef;
      shadowItem = new Item(itemRef.x, itemRef.y, itemRef.w, itemRef.h);
      shadowItem.padding = padding;
      this.updateShadow();
    },
    updateShadow(itemRelativeMouseX, itemRelativeMouseY) {
      // if there is new relativeMouse value that means that we are moving
      if (itemRelativeMouseX != null && itemRelativeMouseY != null) {
        const collapsed = collapseMousePosition(shadowItemRef, {
          x: itemRelativeMouseX,
          y: itemRelativeMouseY,
        });
        
        let coords = pointAsCoordinates(
          collapsed.x,
          collapsed.y
        )
        
        // Min, max is for the shadow to not overflow out of the grid boundaries
        shadowItem.x = Math.max(0,Math.min(coords.x, xCount - shadowItem.w));
        shadowItem.y = Math.max(0,Math.min(coords.y, yCount - shadowItem.h));
      }

      //otherwise we just update size by the reference item

      shadowItem.w = Grid.pixelSizeToUnitLength(
        shadowItemRef.pixelWidth,
        padding,
        shadowItemRef.unitSize,
      );
      shadowItem.h = Grid.pixelSizeToUnitLength(
        shadowItemRef.pixelHeight,
        padding,
        shadowItemRef.unitSize,
      );

      // then we position item at the right place
      positionGridItem(shadowItem);
    },
    getShadowPos() {
      return {x: shadowItem.x, y: shadowItem.y};
    },
    getShadowItem() {
      return shadowItem;
    },
    resetShadow() {
      shadowItem = null;
      shadowItemRef = null;
    },
  };

  setContext(GRID_CONTEXT_NAME, utilityFunctions);
  let newGridItems = [];

  onMount(() => {
    resizeObserver = new ResizeObserver((entries) => {
      const rect = entries[0].contentRect;
      width = rect.width;
      height = rect.height;
      itemWidthFunctions.updateWidth();

      refreshItems();
    });

    grid.setNewGridItemCallback((gridItems) => {
      newGridItems = gridItems;
    });

    for (const newItems of items) {
      grid.newGridItem(
        newItems.component,
        newItems.props,
        newItems.x,
        newItems.y,
        newItems.w,
        newItems.h,
      );
    }

    resizeObserver.observe(rootElement);

    // Cleanup observer on component destroy
    return () => {
      resizeObserver.disconnect();
    };
  });

  function refreshItems() {
    grid.gridItemRefs.forEach((/** @type {Item} */ gridItem) => {
      positionGridItem(gridItem);
      gridItem.mounted = true;
    });
  }

  /**
   * Functions for positioning grid item might be a separate file in the future
   * Keep interpolation at minimum
   */

  /**
   * Positions grid item by its relative values to the absolute position
   * @param gridItem {Item}
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
    return {x: xCoord, y: yCoord};
  }

  /**
   * Method 'collapses' relative mouse position so that mouse seems always in first square in itemgrid
   * @param item {Item}
   * @param {Object} mousePosition - The mouse position.
   * @param {number} mousePosition.x - The x-coordinate of the mouse position.
   * @param {number} mousePosition.y - The y-coordinate of the mouse position.
   *
   */
  function collapseMousePosition(item, mousePosition) {
    let xCoord;
    if (mousePosition.x < item.unitSize + padding / 2) {
      xCoord = item.xCoordinate + mousePosition.x;
    } else {
      xCoord =
        item.xCoordinate + mousePosition.x - (item.w - 1) * item.unitSize;
    }

    let yCoord;
    if (mousePosition.y < item.unitSize + padding / 2) {
      yCoord = item.yCoordinate + mousePosition.y;
    } else {
      yCoord =
        item.yCoordinate + mousePosition.y - (item.h - 1) * item.unitSize;
    }

    return {x: xCoord, y: yCoord};
  }

  export function serialize() {
    let serialized = [];
    for (const gridItemFunc of Object.values(saveDataFunctions)) {
      serialized.push(gridItemFunc());
    }
    return serialized;
  }

  let saveDataFunctions = {};
  setContext("grid", {
    /**
     * @param fun {function}
     * @param id gridItem id
     */
    setDataExportFunction(fun, id) {
      saveDataFunctions[id] = fun;
    },
  });
</script>

<div bind:this={rootElement} class="relative {className}">
  {#each newGridItems as item, i (item.id)}
    <!-- x-1 and y-1 are temporary, after collision detection they will be custom values-->
    <GridItem x={item.x} y={item.y} id={item.id} w={item.w} h={item.h}>
      <svelte:component this={item.component} {...item.props}
      ></svelte:component>
    </GridItem>
  {/each}

  {#if shadowItem}
    <Shadow
      xCoordinate={shadowItem.xCoordinate}
      yCoordinate={shadowItem.yCoordinate}
      pixelWidth={shadowItem.pixelWidth}
      pixelHeight={shadowItem.pixelHeight}
      className="bg-slate-400"
    ></Shadow>
  {/if}
  <slot/>
</div>
