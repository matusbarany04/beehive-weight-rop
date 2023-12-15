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

  /**
   * @type {Object}
   */
  export let cardStates;
  
  export let contentEditable;
  export let className = "";

  let component = "BarChart";
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
    // console.log("cardStates.data.length === beehives.length",cardStates.data, cardStates.data.length, Object.keys(beehives).length)
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

    console.log(
      "beehiveData.map((val)=> val.name)",
      beehiveData.map((val) => val.name),
    );

    let initOptions = () => {
      let option = {
        grid: {
          left: "5%", // Adjust the left margin
          right: "5%", // Adjust the right margin
          top: "1%", // Adjust the top margin
          bottom: "5%", // Adjust the bottom margin
          containLabel: true,
        },
        calculable: true,
        yAxis: {
          type: "value",
          axisLabel: {
            formatter: function (value) {
              // Format the value to have a maximum of two decimal places
              return value.toFixed(2);
            },
          },
        },
        xAxis: {
          type: "category",
          data: beehiveData.map((val) => val.name),
          axisLabel: {
            interval: 0, // Show all labels
            rotate: 10, // Adjust the rotation angle as needed
          },
        },
        series: [
          {
            type: "bar",
            data: beehiveData.map((val) => ({
              value: val.value.toFixed(2),
              label: {
                show: true,
                position: "bottom",
                textStyle: {
                  color: "black",
                  fontSize: 12,
                },
              },
            })),
            itemStyle: {
              barBorderRadius: [5, 5, 0, 0],
            },
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
  {contentEditable}
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
