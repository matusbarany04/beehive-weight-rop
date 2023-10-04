<script>
  import { getGridContext } from "./Grid.svelte";
  import { Item } from "./item";
  import Button from "../../../../../components/Buttons/Button.svelte";
  import { Dragger } from "./dragger";
  import { onMount } from "svelte";

  export let xCoordinate = 1;
  export let yCoordinate = 1;
  export let pixelWidth = 10;
  export let pixelHeight = pixelWidth;

  export let className = "";
</script>

<div
  class="absolute {className}"
  style:width="{pixelWidth}px"
  style:height="{pixelHeight}px"
  style:top="{yCoordinate}px"
  style:left="{xCoordinate}px"
>
  <slot />

  <!--  <p>pixelHeight {pixelHeight}</p>-->
  <!--  <p>pixelWidth: {pixelWidth}</p>-->

  <!--  <p>xPos {xCoordinate}</p>-->
  <!--  <p>yPos: {yCoordinate}</p>-->
</div>
