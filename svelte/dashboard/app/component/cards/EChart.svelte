<script>
  import { onMount, tick } from "svelte";
  import * as echarts from "echarts/dist/echarts.js";
  import shared from "../../stores/shared";
  import CardRoot from "./components/CardRoot.svelte";
  import { generateUUID } from "../../../../components/lib/utils/staticFuncs";
  import ButtonSmall from "../../../../components/Buttons/ButtonSmall.svelte";
  import DropdownInput from "../../../../components/Inputs/DropdownInput.svelte";
  import { BeehiveObj } from "../../stores/Beehive";

  /**
   * @type {Object}
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
  // otrasne fraby ale zatial stačia na rozpoznanie čiarok
  const chartColors = ["#db9834", "#3c7cdc", "#860707", "#245b00"];
  let myChart;

  const beehiveData = [];

  if (
    cardStates.data == null ||
    cardStates.data === "dummy" ||
    cardStates.data == []
  ) {
    console.log("CardStates", cardStates);
    // noDataError is replaced with all weight from all devices
    // error = "NoDataError";
    cardStates.data = [];

    let beehives = shared.getBeehives();
    console.log("beehives", beehives);
    for (const key of Object.keys(beehives)) {
      let beehive = beehives[key];
      cardStates.data.push({
        timespan: "week",
        name: "Váha " + beehive.name,
        type: "weight",
        beehive_id: beehive.beehive_id,
      });
    }
    console.log("cardstates", cardStates.data);
  }

  cardStates.data.forEach((element) => {
    // if (element.type === "dummy") {
    //   // ONLY FOR DEBUG BUG BUG element.type ===  "dummy"
    //   beehiveData.push({
    //     name: "temperature",
    //     data: [
    //       [1717357190000, 10],
    //       [1717357200000, 41],
    //       [1717357210000, 35],
    //       [1717357220000, 51],
    //       [1717357230000, 49],
    //       [1717357240000, 62],
    //       [1717357250000, 69],
    //       [1717357260000, 91],
    //       [1717357270000, 148],
    //     ],
    //   });
    // } else {
    //we load all data
    /** @type {BeehiveObj} */
    let beehiveObject = shared.getBeehiveById(element.beehive_id);
    if (beehiveObject == null) {
      console.error(
        "No Data error ",
        shared.getBeehiveById(element.beehive_id),
      );
      error = "NoDataError";
    } else {
      // non-detachable types have array right under them
      if (!BeehiveObj.isTypeDetachable(element.type)) {
        let data = beehiveObject.getAllDataByType(element.type);
        let timestamp = beehiveObject.getTimestamps();

        // Check if data and timestamp arrays have the same length
        if (data.length !== timestamp.length) {
          throw new Error("Data and timestamp arrays have different lengths");
        }

        // join data and timestamp
        let combinedData = data.map((item, index) => [
          timestamp[index],
          item === -999 ? null : item,
        ]);

        // join data and timestamp like so [[data, timestamp], [data,timestamp]]

        beehiveData.push({
          name: element.name,
          data: combinedData,
        });
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
            timestamp[index + parseInt(dataItem.from)],
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
        data: line.data.map(function (item) {
          return item[1];
        }),
      });
    }

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
      grid: {
        // left: "6%",
        right: "5%",
        top: "4%",
      },
      xAxis: {
        data: beehiveData[0]?.data?.map(function (item) {
          return item[0] != null && item && item.length > 1 ? item[0] : "";
        }),
        axisLabel: {
          type: "time",
          formatter: function (value) {
            const date = new Date(parseInt(value));
            return `${date.toLocaleDateString()} ${date.toLocaleTimeString()}`;
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
        boundaryGap: false,
      },
      yAxis: {},
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
        value={cardStates.data[0]?.type ?? "weight"}
        options={[
          ["weight", "Váha"],
          ["temperature", "Teplota"],
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
        options={[["all", "all"], ...shared.getBeehiveIdsWithNames()]}
      />
      <!--  TODO ERROR add back options without crashing the whole thing -->
      <!--  -->
    {/if}
  </div>
</CardRoot>
