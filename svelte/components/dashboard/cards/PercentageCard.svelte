<script>
  import CardRoot from "./components/CardRoot.svelte";
  import { dataHandler } from "./dataHandler";
  import shared from "../../../dashboard/app/stores/shared";

  export let cardStates;
  export let onDragEnd; // function
  export let onDragStart; // function
  let component = "PercentageCard";
  let error;
  let w = 10;
  let h;
  $: size = Math.min(h, w) / 2;

  let value = 0;
  let comparisonPerc;

  if (cardStates?.data == null) {
    error = "NoDataError";
  } else {
    const beehiveData = [];
    cardStates.data.forEach((element) => {
      if (element.type == "dummy") {
        value = "*";
      } else {
        value = 16;
        console.log("getStatusByType", element.type, element.beehive_id);
        let beeData = shared.getStatusesByType(
          element.type,
          element.beehive_id,
        );
        if (beeData.length > 0) {
          value = beeData[beeData.length - 1] ?? "error";

          let lastVal = beeData[beeData.length - 1][1];
          comparisonPerc = Number(
            value / (parseInt(lastVal) / 100) - 100,
          ).toFixed(0);

          if (value !== "error") {
            if (element.type === "battery") {
              value += "%";
            } else if (element.type === "humidity") {
              value += "%";
            }
            if (element.type === "weight") {
              value += "kg";
            } else if (element.type === "temperature") {
              value += "°C";
            }
          }
        } else {
          value = 0;
        }
      }
    });
  }
</script>

<!-- theme="dashed" -->
<CardRoot {error} {component} {cardStates} {onDragStart} {onDragEnd}>
  <div
    class="flex h-full w-full flex-1 items-center justify-center"
    bind:clientWidth={w}
    bind:clientHeight={h}
  >
    <div
      class="mb-4 flex items-center justify-center font-bold"
      style="width: {w}px"
    >
      <h1
        class="overflow-hidden text-ellipsis whitespace-nowrap text-center"
        style="font-size: {(size * 5) / 6}px"
      >
        {value}
      </h1>
    </div>
  </div>
  <!-- style="font-size: {size / 8}px" -->
  {#if !isNaN(parseFloat(comparisonPerc))}
    <div
      class="absolute bottom-4 box-border flex w-full items-center justify-center"
      style="gap: {Math.min(w / 40, 20)}px; padding-left: {w / 40}px"
    >
      <div
        class="image h-5 w-5 {comparisonPerc > 0 ? 'positive' : 'negative'}"
      />
      <p class="percent no_wrap whitespace-nowrap text-sm">{comparisonPerc}%</p>
      <small class="overflow-hidden text-ellipsis whitespace-nowrap">
        <!-- style="font-size: {size / 8}px" -->
        od posledného týždňa
      </small>
    </div>
  {/if}
</CardRoot>

<style lang="scss">
  .image {
    aspect-ratio: 1/1;
    background-position: center;
    background-repeat: no-repeat;
    background-size: contain;
    background-image: url("/icons/add.svg");
  }

  .positive {
    background-image: url("/icons/graph_positive.svg");
  }

  .negative {
    background-image: url("/icons/graph_negative.svg");
  }
</style>
