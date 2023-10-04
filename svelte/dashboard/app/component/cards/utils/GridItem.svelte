<script>
  import { getGridContext } from "./Grid.svelte";
  import { Item } from "./item";
  import Button from "../../../../../components/Buttons/Button.svelte";
  import { Dragger } from "./dragger";
  import { onMount } from "svelte";

  export let x = 1;
  export let y = 1;
  export let w = 1;
  export let h = 1;

  export let className = "";

  let item = new Item(x, y, w, h);

  let gridRoot = getGridContext();
  let gridItemRoot;

  item.setValueChangedCallback(() => {
    item = item;
  });

  gridRoot.subscribeItem(item);

  onMount(() => {
    let dragger = new Dragger(gridItemRoot, item, gridRoot.getRootElementRef());

    item.subscribeMounted(() => {
      dragger.setDraggable(item.draggable);
      item.setDraggableChangedEvent((draggable) => {
        dragger.setDraggable(draggable);
      });

      const initialX = item.xCoordinate;
      const initialY = item.yCoordinate;

      dragger.setOnMouseDownEvent(() => {
        gridRoot.setShadow(item);
      });

      dragger.setOnMouseMoveEvent((x, y) => {
        gridRoot.updateShadow(x, y);
      });

      dragger.setOnMouseUpEvent((event) => {
        gridRoot.resetShadow();

        gridRoot.requestNewPosition(
          item,
          dragger.getMouseToItemPosition(event),
        );

        // Reset the item's coordinates to the initial values
        // item.xCoordinate = initialX;
        // item.yCoordinate = initialY;
      });
    });
  });
</script>

<!--outline outline-primary-100 -->
<div
  bind:this={gridItemRoot}
  class="absolute {className}"
  style:height="{item.pixelWidth}px"
  style:width="{item.pixelHeight}px"
  style:top="{item.yCoordinate}px"
  style:left="{item.xCoordinate}px"
>
  <slot />
  <!--    <p>x: {item.x}</p>-->
  <!--    <p>y: {item.y}</p>-->
  <!--    <p>w: {item.w}</p>-->
  <!--    <p>h: {item.h}</p>-->
  <!--    <p>pixelHeight {item.pixelHeight}</p>-->
  <!--    <p>pixelWidth: {item.pixelWidth}</p>-->

  <!--    <p>xPos {item.xCoordinate}</p>-->
  <!--    <p>yPos: {item.yCoordinate}</p>-->
</div>
