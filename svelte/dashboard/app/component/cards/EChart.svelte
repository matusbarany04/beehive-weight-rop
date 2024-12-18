<script>
  import { onMount, tick } from "svelte";
  import * as echarts from "echarts/dist/echarts.js";
  import shared from "../../stores/shared";
  import CardRoot from "./components/CardRoot.svelte";
  import {
    generateUUID,
    nowMinusTimeString,
  } from "../../../../components/lib/utils/staticFuncs";
  import ButtonSmall from "../../../../components/Buttons/ButtonSmall.svelte";
  import DropdownInput from "../../../../components/Inputs/DropdownInput.svelte";
  import { BeehiveObj } from "../../stores/Beehive";
  import BeehiveTypeForm from "./forms/BeehiveTypeForm.svelte";
  import MultiselectBeehiveForm from "./forms/MultiselectBeehiveForm.svelte";

  /**
   * @type {Object}
   */
  export let cardStates;

  export let contentEditable = true;

  export let className = "";

  let component = "LineGraph";
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
  // otrasne farby, treba zmeniť, ale zatial stačia na rozpoznanie čiarok
  const chartColors = ["#db9834", "#3c7cdc", "#860707", "#245b00"];

  let myChart;
  let allSelected = false;
  let beehiveData = [];
  let beehivelist = cardStates.data;

  let filterTime = 0;

  function getDataFromBeehive(beehive_id, type, minTimespan = 0, name = "") {
    console.log("loading data with minTimespan", minTimespan);
    // try to load beehive object from its key
    let beehiveObject = shared.getBeehiveById(beehive_id);

    if (beehiveObject == null) {
      console.error("Wrong id error ", beehive_id);
      return null;
    } else {
      // non-detachable types have array right under them
      if (!BeehiveObj.isTypeDetachable(type)) {
        let data = beehiveObject.getAllDataByType(type);
        let timestamp = beehiveObject.getTimestamps();

        // Check if data and timestamp arrays have the same length
        if (data.length !== timestamp.length) {
          throw new Error("Data and timestamp arrays have different lengths");
        }

        let filtetCount = 0;
        // join data and timestamp
        let combinedData = [];
        data.forEach((item, index) => {
          if (timestamp[index] >= minTimespan) {
            combinedData.push([timestamp[index], item === -999 ? null : item]);
          } else {
            filtetCount++;
          }
        });
        console.log("number of filtered", filtetCount);

        // join data and timestamp like so [[data, timestamp], [data,timestamp]]

        return [
          {
            name: name,
            data: combinedData,
          },
        ];
      }
      // detachable - connector types have nested array underneath them
      else {
        let data = beehiveObject.getAllDataByType(type);
        let timestamp = beehiveObject.getTimestamps();

        let output = [];
        for (const dataItem of data) {
          // Check if data and timestamp arrays have the same length
          if (dataItem.values.length + dataItem.from > timestamp.length) {
            throw new Error(
              `Data of length ${dataItem.values.length} from ts index ${dataItem.from} is longer than timestamp length ${timestamp.length}`,
            );
          }

          // join data and timestamp
          let combinedData = dataItem.values.map((item, index) => [
            timestamp[index + parseInt(dataItem.from)],
            item === -999 ? null : item,
          ]);

          output.push({
            name: name,
            data: combinedData,
          });
        }

        return output;
      }
    }
  }

  function loadData(minTimespan = "eternity") {
    beehiveData = [];
    let beehives = shared.getBeehives();
    if (beehivelist.length === Object.keys(beehives).length) {
      allSelected = true;
    }

    if (
      beehivelist == null ||
      beehivelist === "dummy" ||
      !beehivelist ||
      beehivelist.length === 0 ||
      beehivelist[0].beehive_id === "all" ||
      beehivelist.length === beehives.length
    ) {
      allSelected = true;

      beehivelist = [
        {
          timespan: minTimespan ?? "week",
          name: "Váha  všetkých zariadení",
          type: BeehiveObj.getPrimaryDataType(),
          beehive_id: ["all"],
        },
      ];
    }

    beehivelist.forEach((element) => {
      //we load all data
      filterTime = nowMinusTimeString(minTimespan ?? element.timespan);
      element.beehive_id.forEach((bee_id) => {
        /** @type {BeehiveObj} */
        if (bee_id === "all") {
          Object.keys(shared.getBeehives()).forEach((beehive_id) => {
            beehiveData.push(
              ...getDataFromBeehive(beehive_id, element.type, filterTime),
            );
          });
        } else {
          beehiveData.push(
            ...getDataFromBeehive(bee_id, element.type, filterTime),
          );
        }
      });
    });
  }

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
    let series = [];
    for (let index = 0; index < beehiveData.length; index++) {
      const line = beehiveData[index];
      series.push({
        lineStyle: {
          color: chartColors[index],
        },
        itemStyle: {
          borderType: "solid",
          color: chartColors[index],
          borderCap: "butt",
          emphasis: {
            color: chartColors[index],
          },
        },
        name: line.name,
        type: "line",
        smooth: true,
        data: line.data.map(function (item) {
          let timestamp = item[0];
          let val = item[1];
          const isoString = new Date(timestamp).toISOString();
          return [isoString, val];
        }),
      });
    }

    let option = {
      title: {
        show: false,
      },
      toolbox: {
        feature: {
          saveAsImage: {},
        },
      },
      tooltip: {
        trigger: "axis",
        formatter: function (params) {
          if (!params[0] || !params[0].values) {
            return "";
          }
          // Assuming params[0].value[0] or params[0].value is the timestamp value
          let value = params[0].value[0] ? params[0].value[0] : params[0].value;

          let date = new Date(value);

          let hours = String(date.getHours()).padStart(2, "0");
          let minutes = String(date.getMinutes()).padStart(2, "0");
          let seconds = String(date.getSeconds()).padStart(2, "0");
          return `${hours}:${minutes}:${seconds}`;
        },
        axisPointer: {
          type: "cross",
          animation: false,
          label: {
            backgroundColor: "#ccc",
            borderColor: "#aaa",
            borderWidth: 1,
            shadowBlur: 0,
            shadowOffsetX: 0,
            shadowOffsetY: 0,
            color: "#222",
          },
        },
      },
      calculable: true,
      grid: {
        right: "5%",
        top: "4%",
      },
      xAxis: {
        type: "time",
        axisLabel: {
          type: "time",
          formatter: function (value) {
            const date = new Date(parseInt(value));
            return `${months[date.getMonth()]}.${date.getDate()}`; // should be like Nov 13
          },
        },
        axisPointer: {
          label: {
            formatter: function (params) {
              const date = new Date(parseInt(params.value));
              return `${date.toLocaleDateString()} ${date.toLocaleTimeString()}`;
            },
          },
        },
      },
      yAxis: {
        data: beehiveData[0]?.data?.map(function (item) {
          return item[1] != null && item && item.length > 1 ? item[1] : "-";
        }),
        type: "value",
      },
      dataZoom: [
        {
          type: "slider",
          show: true,
          // xAxisIndex: [0], // Controls the first xAxis by default
          start: 0, // Initial start percentage
          end: 100, // Initial end percentage

          // Soft gray background for the slider
          backgroundColor: "rgba(240, 240, 240, 0.6)",

          // Subtle border color
          borderColor: "rgba(220, 220, 220, 1)",
          borderWidth: 0,

          // Muted highlight for the selected area
          fillerColor: "rgba(220, 220, 220, 0.8)",

          // Soft and muted data shadow styles
          dataBackground: {
            lineStyle: {
              color: "rgba(126,83,7,0.42)",
              width: 1,
            },
            areaStyle: {
              color: "rgba(230, 230, 230, 0.8)",
            },
          },
          // Customize the brush-style drag area
          brushStyle: {
            color: "rgba(240, 240, 240, 0.2)", // Soft white for the main color
            borderWidth: 0,
            shadowColor: "rgba(150, 150, 150, 0.3)", // Medium gray for the shadow
            shadowOffsetX: 2,
            shadowOffsetY: 2,
          },

          // Handle styling: muted gray with soft shadow
          handleStyle: {
            color: "rgb(219,152,52)",
            borderWidth: 0,
            shadowBlur: 4,
            shadowOffsetX: 2,
            shadowOffsetY: 2,
            shadowColor: "rgba(150, 150, 150, 0.5)",
          },

          // TODO get data on index and display the date
          labelFormatter: function (value) {
            const date = new Date(parseInt(value));
            return `${date.toLocaleDateString()} ${date.toLocaleTimeString()}`;
          },
          textStyle: {
            color: "rgba(100, 100, 100, 1)",
          },
        },
      ],
      series: series,
    };

    return option;
  };

  function refreshOptions() {
    let option = initOptions();
    myChart.setOption(option);
  }

  try {
    loadData();

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

  function changeZoom(timeString) {
    filterTime = nowMinusTimeString(timeString);
    loadData(timeString);
    refreshOptions();
  }
</script>

<CardRoot
  {className}
  {contentEditable}
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
  <div
    slot="header"
    class="flex w-full max-w-[15rem] items-center justify-around"
  >
    {#if myChart}
      {#each miniButtons as item}
        <ButtonSmall
          text={item[1]}
          type={headerSelected === item[0] ? "primary" : "secondary"}
          onClick={() => {
            headerSelected = item[0];
            changeZoom(item[0]);
          }}
        />
      {/each}
    {/if}
  </div>

  <div class="relative flex max-h-full w-full">
    <div {id} class="h-full w-full" />
  </div>
  <!--//beehivelist[0]?.type || "weight"}-->
  <div class="" slot="customSettings">
    {#if beehivelist != null}
      <!--      <BeehiveTypeForm-->
      <!--        typeChoice={"battery"}-->
      <!--        beehive_value={allSelected-->
      <!--          ? "all"-->
      <!--          : beehivelist[0]?.beehive_id || "all"}-->
      <!--        beehiveId={beehivelist[0]?.beehive_id}-->
      <!--      />-->

      <MultiselectBeehiveForm
        typeChoice={beehivelist[0]?.type ?? "weight"}
        beehive_value={allSelected
          ? "all"
          : beehivelist[0]?.beehive_id ?? "all"}
        beehiveId={beehivelist[0]?.beehive_id}
      ></MultiselectBeehiveForm>

      <DropdownInput
        label="Úsek načítaných dát"
        name="timespan"
        value={beehivelist[0]?.timespan ?? "week"}
        small={"Upozornenie: väčšieho množstva dát môže spôsobiť dlhšie načítanie stránky a problémy v systémoch s obmedzenými zdrojmi. Prosím, zvážte to pri výbere obdobia."}
        options={[
          ["week", "Posledný týždeň"],
          ["month", "Posledný mesiac"],
          ["year", "Posledný rok"],
        ]}
      />
    {/if}
  </div>
</CardRoot>
