<script>
  import CardRoot from "./components/CardRoot.svelte";
  import { modes, getDefaultMode } from "./components/cardConfig";
  import { dataHandler } from "./dataHandler";

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

        let beeData = dataHandler.getDataByType(
          element.type,
          element.beehive_id,
          true,
          element.from,
          element.to,
        );
        if (beeData.length > 0) {
          value = beeData[0][1] ?? "error";

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
    class="w-full h-full flex-1 flex items-center justify-center"
    bind:clientWidth={w}
    bind:clientHeight={h}
  >
    <div
      class="font-bold flex items-center justify-center mb-4"
      style="width: {w}px"
    >
      <h1
        class="text-center whitespace-nowrap text-ellipsis overflow-hidden"
        style="font-size: {(size * 5) / 6}px"
      >
        {value}
      </h1>
    </div>
  </div>
  <!-- style="font-size: {size / 8}px" -->
  {#if !isNaN(parseFloat(comparisonPerc))}
    <div
      class="absolute bottom-4 w-full flex items-center justify-center box-border"
      style="gap: {Math.min(w / 40, 20)}px; padding-left: {w / 40}px"
    >
      <div
        class="w-5 h-5 image {comparisonPerc > 0 ? 'positive' : 'negative'}"
      />
      <p class="percent no_wrap whitespace-nowrap text-sm">{comparisonPerc}%</p>
      <small class="whitespace-nowrap text-ellipsis overflow-hidden">
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
