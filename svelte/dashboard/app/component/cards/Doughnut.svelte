<script>
  import {onMount, tick} from "svelte";
  import * as echarts from "echarts/dist/echarts.js";
  import shared from "../../stores/shared";
  import CardRoot from "./components/CardRoot.svelte";
  import {generateUUID} from "../../../../components/lib/utils/staticFuncs";
  import ButtonSmall from "../../../../components/Buttons/ButtonSmall.svelte";
  import DropdownInput from "../../../../components/Inputs/DropdownInput.svelte";
  import {BeehiveObj} from "../../stores/Beehive";
  import BeehiveTypeForm from "./forms/BeehiveTypeForm.svelte";
  import MultiselectBeehiveForm from "./forms/MultiselectBeehiveForm.svelte";

  /**
   * @type {Object}
   */
  export let cardStates;

  export let className = "";

  let component = "Doughnut";
  let id = generateUUID();
  let error = null;
  let headerSelected = "none";
  let miniButtons = [
    ["hour", "1H"],
    ["day", "1D"],
    ["week", "1T"],
    ["month", "1M"],
    ["year", "1R"],
  ];
  // otrasne fraby ale zatial stačia na rozpoznanie čiarok
  const chartColors = ["#db9834", "#3c7cdc", "#860707", "#245b00"];
  let myChart;
  let allSelected = false;
  const beehiveData = [
    {value: 1048, name: "Search Engine"},
    {value: 735, name: "Direct"},
    {value: 580, name: "Email"},
    {value: 484, name: "Union Ads"},
    {value: 300, name: "Video Ads"},
  ]
  
  let beehivelist = cardStates.data;

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

      beehivelist = [];
    }

    beehivelist.forEach((element) => {
      //we load all data
      /** @type {BeehiveObj} */
      let beehiveObject = shared.getBeehiveById(element.beehive_id);
      if (beehiveObject == null) {
        console.error(
          "No Data error ",
          shared.getBeehiveById(element.beehive_id) + " " + element.beehive_id,
        );
        error = "NoDataError";
      } else {
        // non-detachable types have array right under them
        if (!BeehiveObj.isTypeDetachable(element.type)) {
          let data = beehiveObject.getLastDataByType(element.type);

          // Check if data and timestamp arrays have the same length
          if (data.length == null) {
            throw new Error("Data and timestamp arrays have different lengths");
          }

          // join data and timestamp


          // join data and timestamp like so  {value: 512, name: "beehive name" }


        }
        // detachable - connector types have nested array underneath them
        else {
          let data = beehiveObject.getAllDataByType(element.type);
          let timestamp = beehiveObject.getTimestamps();

          for (const dataItem of data) {
            // Check if data and timestamp arrays have the same length
            if (dataItem.values.length + dataItem.from - 1 > timestamp.length) {
              throw new Error(
                `Data of length ${dataItem.values.length} from ts index ${dataItem.from} is longer than timestamp length ${timestamp.length}`,
              );
            }

            // join data and timestamp
            let combinedData = dataItem.values.map((item, index) => [
              timestamp[index + parseInt(dataItem.from) - 1],
              item === -999 ? null : item,
            ]);

            beehiveData.push({
              name: element.name,
              data: combinedData,
            });
          }
        }
      }
    });

    let initOptions = () => {
      const months = [
        "Jan",
        "Feb",
        "Mar",
        "Apr",
        "May",
        "Jun",
        "Jul",
        "Aug",
        "Sep",
        "Oct",
        "Nov",
        "Dec",
      ];


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
            data: beehiveData
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

  let resizeEvent = () => {
  };
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
          beehive_id: formData.get("beehive_id"),
        },
      ],
    };
  }}
  {component}
  {cardStates}
  {error}
>
  <div class="relative flex max-h-full w-full">
    <div {id} class="h-full w-full"/>
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
