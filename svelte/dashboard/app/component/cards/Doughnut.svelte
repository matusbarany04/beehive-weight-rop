<script>
  import { onMount, tick } from "svelte";
  import * as echarts from "echarts/dist/echarts.js";
  import shared from "../../stores/shared";
  import CardRoot from "./components/CardRoot.svelte";
  import { generateUUID } from "../../../../components/lib/utils/staticFuncs";
  import ButtonSmall from "../../../../components/Buttons/ButtonSmall.svelte";
  import DropdownInput from "../../../../components/Inputs/DropdownInput.svelte";
  import { BeehiveObj } from "../../stores/Beehive";
  import BeehiveTypeForm from "./forms/BeehiveTypeForm.svelte";
  import MultiselectBeehiveForm from "./forms/MultiselectBeehiveForm.svelte";

  export let contentEditable;
  /**
   * @type {Object}
   */
  export let cardStates;

  export let className = "";

  let component = "Doughnut";
  let id = generateUUID();
  let error = null;

  let myChart;
  let allSelected = false;
  const beehiveData = [];

  let beehivelist = cardStates.data;

  function getDataFromBeehive(beehive_id, type) {
    // console.log("getDataFromBeehive", beehive_id);
    /** @type {BeehiveObj} */
    let beehiveObject = shared.getBeehiveById(beehive_id);
    if (beehiveObject == null) {
      console.error(
        "No Data error ",
        beehiveObject,
        shared.getBeehiveById(beehive_id) + " " + beehive_id,
      );
      error = "NoDataError";
      return null;
    } else {
      return {
        name: beehiveObject.name,
        value: beehiveObject.getLastDataByType(type),
      };
    }
  }

  try {
    let beehives = shared.getBeehives();
    if (beehivelist.length === Object.keys(beehives).length) {
      allSelected = true;
    }

    if (
      beehivelist == null ||
      beehivelist === "dummy" ||
      beehivelist == [] ||
      beehivelist[0].beehive_id === "all" ||
      beehivelist.length === beehives.length
    ) {
      allSelected = true;

      beehivelist = [
        {
          beehive_id: ["all"],
          type: BeehiveObj.getPrimaryDataType(),
        },
      ];
    }

    beehivelist.forEach((element) => {
      //we load all data
      //  beehive id is and array in this card
      for (const beehive_id of element.beehive_id) {
        if (beehive_id === "all") {
          // shared.getBeehives is an array
          Object.keys(shared.getBeehives()).forEach((bee_id) => {
            beehiveData.push(getDataFromBeehive(bee_id, element.type));
          });
        } else {
          beehiveData.push(getDataFromBeehive(beehive_id, element.type));
        }
      }
    });

    let initOptions = () => {
      let option = {
        tooltip: {
          trigger: "item",
        },
        legend: {
          top: "5%",
          left: "center",
        },
        series: [
          {
            name: "Access From",
            type: "pie",
            radius: ["40%", "70%"],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: "#fff",
              borderWidth: 2,
            },
            label: {
              show: false,
              position: "center",
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 34,
                fontWeight: "bold",
              },
            },
            labelLine: {
              show: false,
            },
            data: beehiveData,
          },
        ],
      };

      return option;
    };

    onMount(() => {
      if (error == null) {
        // Use a Svelte ref for the chart container
        myChart = echarts.init(document.getElementById(id));

        let option = initOptions();

        myChart.setOption(option);
        myChart.resize();
        tick().then(() => {
          myChart.resize();
        });

        resizeEvent = () => {
          myChart.resize();
        };
      }
    });
  } catch (e) {
    console.error("LineChart error");
    console.error(e);
  }

  let resizeEvent = () => {};
</script>

<CardRoot
  {className}
  resizedEvent={resizeEvent}
  updateSettings={(formData) => {
    return {
      status: "success",
      data: [
        {
          timespan: formData.get("timespan"),
          name: formData.get("data_type"), // TODO make translatable
          type: formData.get("data_type"),
          beehive_id: formData.getAll("beehive_id"),
        },
      ],
    };
  }}
  {contentEditable}
  {component}
  {cardStates}
  {error}
>
  <div class="relative flex max-h-full w-full">
    <div {id} class="h-full w-full" />
  </div>

  <div class="" slot="customSettings">
    {#if beehivelist != null}
      <MultiselectBeehiveForm
        typeChoice={beehivelist[0]?.type ?? "weight"}
        beehive_value={allSelected
          ? "all"
          : beehivelist[0]?.beehive_id ?? "all"}
        beehiveId={beehivelist[0]?.beehive_id}
      ></MultiselectBeehiveForm>
    {/if}
  </div>
</CardRoot>
