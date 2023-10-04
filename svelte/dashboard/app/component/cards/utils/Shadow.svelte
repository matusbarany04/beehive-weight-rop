<script>
  import { spring } from "svelte/motion";

  let _coordinates = spring(
    { x: 1, y: 1 },
    {
      stiffness: 0.1,
      damping: 0.25,
    },
  );

  let _pixelSize = spring(
    { w: 10, h: 10 },
    {
      stiffness: 0.1,
      damping: 0.25,
    },
  );

  export let xCoordinate = 1;
  export let yCoordinate = 1;
  export let pixelWidth = 10;
  export let pixelHeight = 10;

  _coordinates.set({ x: xCoordinate, y: yCoordinate });
  _pixelSize.set({ w: pixelWidth, h: pixelHeight });

  export let className = "";

  $: _coordinates.set({ x: xCoordinate, y: yCoordinate });
  $: _pixelSize.set({ w: pixelWidth, h: pixelHeight });
</script>

<div
  class="absolute {className}"
  style:width="{$_pixelSize.w}px"
  style:height="{$_pixelSize.h}px"
  style:left="{$_coordinates.x}px"
  style:top="{$_coordinates.y}px"
>
  <slot />

  <!--  <p>pixelHeight {pixelHeight}</p>-->
  <!--  <p>pixelWidth: {pixelWidth}</p>-->

  <!--  <p>xPos {xCoordinate}</p>-->
  <!--  <p>yPos: {yCoordinate}</p>-->
</div>
