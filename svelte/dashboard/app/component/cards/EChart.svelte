<script>
  import { onMount } from "svelte";
  import * as echarts from "echarts/dist/echarts.js";
  import shared, { onLoad } from "../../stores/shared";
  import CardRoot from "./components/CardRoot.svelte";
  import { generateUUID } from "../../../../components/lib/utils/staticFuncs";
  import ButtonSmall from "../../../../components/Buttons/ButtonSmall.svelte";
  import DropdownInput from "../../../../components/Inputs/DropdownInput.svelte";
  import { tick } from "svelte";

  /**
   * @type {object}
   */
  export let cardStates;

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

  let myChart;

  const beehiveData = [];

  if (cardStates.data == null) {
    console.log("CardStates", cardStates);
    error = "NoDataError";
  } else {
    cardStates.data.forEach((element) => {
      console.log("eachart", element);
      if (element.type === "dummy") {
        // ONLY FOR DEBUG BUG BUG element.type ===  "dummy"
        beehiveData.push({
          name: "temperature",
          data: [
            [1717357190000, 10],
            [1717357200000, 41],
            [1717357210000, 35],
            [1717357220000, 51],
            [1717357230000, 49],
            [1717357240000, 62],
            [1717357250000, 69],
            [1717357260000, 91],
            [1717357270000, 148],
          ],
        });
      } else {
        console.log("element ", element);
        beehiveData.push({
          name: element.name,
          data:
            // wrong function, but it at least doesnt crash
            shared
              .getBeehiveById(element.beehive_id)
              .getAllDataByType(element.type),
        });

        console.log(beehiveData);
      }
    });

    let initOptions = () => {
      return {
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
            // Assuming params[0].value[0] or params[0].value is the timestamp value
            let value = params[0].value[0]
              ? params[0].value[0]
              : params[0].value;

            let date = new Date(value / 1000);

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
        grid: {
          // left: "6%",
          right: "5%",
          top: "4%",
        },
        xAxis: {
          data: beehiveData[0]?.data.map(function (item) {
            return item[0];
          }),
          axisLabel: {
            type: "time",
            formatter: function (value) {
              // Assuming value is in milliseconds
              let date = new Date(value / 1000);

              // Format it to HH:mm:ss or any format you prefer
              let hours = String(date.getHours()).padStart(2, "0");
              let minutes = String(date.getMinutes()).padStart(2, "0");
              let seconds = String(date.getSeconds()).padStart(2, "0");
              return `${hours}:${minutes}:${seconds}`;
            },
          },
          boundaryGap: false,
        },
        yAxis: {},
        dataZoom: [
          {
            type: "slider",
            show: true,
            xAxisIndex: [0], // Controls the first xAxis by default
            start: 10, // Initial start percentage
            end: 80, // Initial end percentage

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

            // Label with soft gray text
            labelFormatter: function (value) {
              return "Value: " + value;
            },
            textStyle: {
              color: "rgba(100, 100, 100, 1)",
            },
          },
        ],
        series: {
          lineStyle: {
            color: "#db9834", // Blue color, for example
          },
          itemStyle: {
            borderType: "solid",
            color: "#db9834",
            borderCap: "butt",
            emphasis: {
              color: "#db9834",
            },
          },
          name: beehiveData[0]?.name,
          type: "line",
          data: beehiveData[0]?.data.map(function (item) {
            return item[1];
          }),
        },
      };
    };

    onMount(() => {
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
    });
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
          beehive_id: formData.get("beehive_id"),
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

  <div class="" slot="customSettings">
    {#if cardStates.data != null}
      <DropdownInput
        label="Typ dát"
        name="data_type"
        value={cardStates.data[0]?.type ?? "dummy"}
        options={[
          ["dummy", "ukážkové dáta"],
          ["temperature", "Teplota"],
          ["weight", "Váha"],
          ["humidity", "Vlhkosť"],
        ]}
      />

      <DropdownInput
        label="Úsek načítaných dát"
        name="timespan"
        value={cardStates.data[0]?.timespan ?? "week"}
        small={"Upozornenie: väčšieho množstva dát môže spôsobiť dlhšie načítanie stránky a problémy v systémoch s obmedzenými zdrojmi. Prosím, zvážte to pri výbere obdobia."}
        options={[
          ["week", "Posledný týždeň"],
          ["month", "Posledný mesiac"],
          ["year", "Posledný rok"],
        ]}
      />

      <DropdownInput
        label="Váha"
        name="beehive_id"
        value={cardStates.data[0]?.beehive_id ?? "all"}
        small={"Váha pre ktorú sa budú zobrazovať dáta"}
        options={[]}
      />
      <!--  TODO ERROR add back options without crashing the whole thing -->
      <!--  options={[["all", "all"], ...shared.getBeehiveIdsWithNames()]}-->
    {/if}
  </div>
</CardRoot>
