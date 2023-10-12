<script>
  import { getGridContext } from "./Grid.svelte";
  import { Item } from "./item";
  import { Dragger } from "./dragger";
  import { onMount } from "svelte";
  import { Resizer } from "./resizer";
  import { Grid } from "./grid";
  import { spring } from "svelte/motion";
  import { setContext, getContext } from "svelte";

  /*
    TODO 
     
    add - remove functionality 
    when in edit mode overlay actual content of gridItem
   */
  export let x = 1;
  export let y = 1;
  export let w = 1;
  export let h = 1;
  export let id = null;

  export let className = "";

  let gridRoot = getGridContext();

  let item = new Item(x, y, w, h, id);

  item.padding = gridRoot.getPadding();

  let gridItemRoot;
  let resizerElement;

  let pixelSize = spring(
    { pixelHeight: item.pixelHeight, pixelWidth: item.pixelWidth },
    {
      stiffness: 0.1,
      damping: 0.4,
    },
  );
  let itemCoords = spring(
    { x: item.xCoordinate, y: item.yCoordinate },
    {
      stiffness: 0.1,
      damping: 0.4,
    },
  );

  item.setValueChangedCallback(() => {
    item = item;
    itemCoords.set({ x: item.xCoordinate, y: item.yCoordinate });
    pixelSize.set({
      pixelHeight: item.pixelHeight,
      pixelWidth: item.pixelWidth,
    });
  });

  gridRoot.subscribeItem(item);

  onMount(() => {
    let dragger = new Dragger(gridItemRoot, item, gridRoot.getRootElementRef());

    let resizer = new Resizer(resizerElement, item, gridItemRoot, gridRoot);

    item.subscribeMounted(() => {
      dragger.setDraggable(item.draggable);

      item.setDraggableChangedEvent((draggable) => {
        dragger.setDraggable(draggable);
        resizer.setResizable(draggable);
      });

      let initialX = item.xCoordinate;
      let initialY = item.yCoordinate;

      dragger.setOnMouseDownEvent(() => {
        initialY = item.yCoordinate;
        initialX = item.xCoordinate;
        gridRoot.setShadow(item);
      });

      dragger.setOnMouseMoveEvent((x, y) => {
        gridRoot.updateShadow(x, y);
        item.zIndex = 5000;
      });

      dragger.setOnMouseUpEvent((event) => {
        gridRoot.resetShadow();

        gridRoot.requestNewPosition(
          item,
          dragger.getMouseToItemPosition(event),
        );

        item.resetZIndex();
      });

      resizer.setOnMouseMoveEvent(() => {
        item.zIndex = 5000;
      });

      resizer.setResizable(item.draggable);

      resizer.setOnMouseUpEvent((event) => {
        gridRoot.requestNewResize(
          item,
          Grid.pixelSizeToUnitLength(
            item.pixelWidth,
            gridRoot.getPadding(),
            item.unitSize,
          ),
          Grid.pixelSizeToUnitLength(
            item.pixelHeight,
            gridRoot.getPadding(),
            item.unitSize,
          ),
        );
        item.resetZIndex();
      });
    });
  });

  let saveDataFunction;
  setContext("gridItem", {
    /**
     * @param fun {function}
     */
    setDataExportFunction(fun) {
      saveDataFunction = fun;
    },
  });

  let gridExport = getContext("grid");
  gridExport.setDataExportFunction(() => {
    return {
      id: item.id,
      x: item.x,
      y: item.y,
      w: item.w,
      h: item.h,
      ...saveDataFunction(),
    };
  }, item.id);
</script>

<!--outline outline-primary-100 -->
<div
  bind:this={gridItemRoot}
  class="absolute {className}"
  style:width="{$pixelSize.pixelWidth}px"
  style:height="{$pixelSize.pixelHeight}px"
  style:left="{$itemCoords.x}px"
  style:top="{$itemCoords.y}px"
  style:z-index={item.zIndex}
>
  <slot />

  <div
    class="overlay top-0 absolute h-full w-full bg-none"
    style:z-index={item.zIndex + 1}
  ></div>

  <div class="absolute right-1 top-0 z-30 {!item.draggable ? 'hidden' : ''}">
    <button
      class="removeImage headerIcon"
      on:click={(event) => {
        gridRoot.deleteGridItem(item);
        event.stopPropagation();
      }}
      on:mousemove={(event) => {
        event.stopPropagation();
      }}
      on:mousedown={(event) => {
        event.stopPropagation();
      }}
      on:mouseup={(event) => {
        event.stopPropagation();
      }}
    ></button>
  </div>

  <div
    bind:this={resizerElement}
    style:background-image="url('/icons/arrows-fullscreen.svg')"
    class="absolute bottom-1.5 right-1.5 z-10 h-4 w-4 {!item.draggable
      ? 'hidden'
      : ''}"
  ></div>
</div>

<style>
  .removeImage {
    width: 20px;
    height: 20px;
    background-size: cover;
    isolation: isolate;
    -webkit-isolation: isolate;
    -webkit-mask-mode: alpha;
    -webkit-mask-size: 100%;
    mask-size: 100%;
    background: red;
    mask-image: url("icons/delete.svg");
    -webkit-mask-image: url("icons/delete.svg");
  }

  .headerIcon {
    height: 80%;
    aspect-ratio: 1/1;
    margin-left: 5px;
    z-index: 10;

    &:hover {
      transform: scale(1.05);
      cursor: pointer;
    }
  }
</style>
