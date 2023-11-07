<script>
  import CardRoot from "./components/CardRoot.svelte";
  import shared from "../../stores/shared";
  import DropdownInput from "../../../../components/Inputs/DropdownInput.svelte";
  import {getUnitByType} from "../../../../components/lib/utils/staticFuncs";

  export let cardStates;

  let component = "PercentageCard";
  let error = null;
  let innerError = null;
  let w = 10;
  let h;
  $: size = Math.min(h, w) / 2 / (innerError == null ? 1 : 1.8);
  let value = 0;

  function loadAllBeehives(type) {
    let beehiveKeys = Object.keys(shared.getBeehives());

    for (const beehiveKey of beehiveKeys) {
      let beehive = shared.getBeehives()[beehiveKey];
      console.log("beehive.getLastDataByType(type)", beehive.getLastDataByType(type))
      value += beehive.getLastDataByType(type);
    }

    value =
      Number(value) === parseInt(value)
        ? Number(value)
        : Number(value).toFixed(1);

    value += getUnitByType(type);
  }

  try {


    if (cardStates.data == "dummy" || cardStates.data == []) {
      cardStates.title = "Súčet všetkých váh";
      cardStates.data = [
        {
          type: "weight",
          beehive_id: "all",
          from: "week",
          till: "now",
          mergeType: "sum",
        },
      ];

      loadAllBeehives("weight")
    }

    if (!cardStates?.data) {
      error = "NoDataError";
    } else {
      cardStates.data.forEach((element) => {
        if (element.beehive_id === "all") {
          loadAllBeehives(element.type)
        } else {

          if (element.type === "dummy") {
            value = ":/";
            return; // continue to the next iteration
          }

          let beeData;
          if (element.data === undefined) {
            beeData = shared
              .getBeehiveById(element.beehive_id)
              .getLastDataByType(element.type);
          } else {
            beeData = element.data;
          }

          if (beeData != null) {
            if (!isNaN(beeData)) {
              value =
                Number(beeData) === parseInt(beeData)
                  ? Number(beeData)
                  : Number(beeData).toFixed(1);

              value = value || "NoData";

              if (value !== "error" && value !== "NoData") {
                if (element.unit === undefined) {
                  value += getUnitByType(element.type);
                } else {
                  value += element.unit;
                }
              } else {
                innerError = "NoData";
              }
            } else {
              value = beeData;
            }
          } else {
            value = "NoData";
            innerError = "NoData";
          }
        }

      });
    }
  } catch (e) {
    console.error("Percentage card error ", e);
    error = "CardStateProcessError";
  }
</script>

<!-- theme="dashed" -->
<CardRoot
  updateSettings={(formData) => {
    return {
      status: "success",
      
      data: [
        {
          name: formData.get("data_type"), // TODO make translatable
          type: formData.get("data_type"),
          beehive_id: formData.get("beehive_id"),
        },
      ],
    };
  }}
  {error}
  {component}
  {cardStates}
>
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
        style="font-size: {(size * 4.5) / 6}px"
      >
        {value}
      </h1>
    </div>
  </div>
  <!--{#if !isNaN(parseFloat(comparisonPerc))}-->
  <!--  <div-->
  <!--    class="absolute bottom-4 box-border flex w-full items-center justify-center"-->
  <!--    style="gap: {Math.min(w / 40, 20)}px; padding-left: {w / 40}px"-->
  <!--  >-->
  <!--    <div-->
  <!--      class="image h-5 w-5 {comparisonPerc > 0 ? 'positive' : 'negative'}"-->
  <!--    />-->
  <!--    <p class="percent no_wrap whitespace-nowrap text-sm">{comparisonPerc}%</p>-->
  <!--    <small class="overflow-hidden text-ellipsis whitespace-nowrap">-->
  <!--      od posledného týždňa-->
  <!--    </small>-->
  <!--  </div>-->
  <!--{/if}-->

  <!--TODO might crash-->

  <div class="" slot="customSettings">
    {#if error == null && value !== "error" && value !== "NoData"}
      <DropdownInput
        label="Typ dát"
        name="data_type"
        value={cardStates.data[0].type || "weight" }
        options={[
          ["weight", "Váha"],
          ["temperature", "Teplota"],
          ["humidity", "Vlhkosť"],
        ]}
      />

      <DropdownInput
        label="Váha"
        name="beehive_id"
        value={cardStates.data[0].beehive_id || "all"}
        small={"Váha pre ktorú sa budú zobrazovať dáta"}
        options={[["all", "all"], ...shared.getBeehiveIdsWithNames()]}
      />
    {/if}
  </div>
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
