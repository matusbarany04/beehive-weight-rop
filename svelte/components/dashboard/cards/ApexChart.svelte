<script>
  import CardRoot from "./components/CardRoot.svelte";
  import { generateUUID } from "../../lib/utils/static";
  import { getContext, onMount } from "svelte";
  import ButtonSmall from "../../Buttons/ButtonSmall.svelte";
  import DropdownInput from "../../Inputs/DropdownInput.svelte";
  import shared from "../../../dashboard/app/stores/shared";

  export let cardStates;
  export let onDragEnd; // function
  export let onDragStart; // function

  let component = "LineGraph";
  let chart;
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

  function changeZoom(value) {
    chart.zoomX(shared.nowMinusFrom(value).getTime(), new Date().getTime());
  }

  if (cardStates?.data == null) {
    console.log("CardStates", cardStates);
    error = "NoDataError";
  } else {
    const beehiveData = [];
    cardStates.data.forEach((element) => {
      console.log(element);
      if (true) {
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
        beehiveData.push({
          name: element.name,
          data: dataHandler.getDataByType(
            element.type,
            element.beehive_id,
            true,
          ),
        });
      }
    });

    onMount(async () => {
      var options = {
        stroke: {
          show: true,
          curve: "smooth",
          // lineCap: "butt",
          width: 2,
          dashArray: 0,
        },
        chart: {
          height: "100%",
          type: "line",
          animations: {
            initialAnimation: {
              enabled: false,
            },
          },
        },

        series: [
          // {
          //   name: "Temperature",
          //   data: temps,
          // },
          ...beehiveData,
        ],
        yaxis: {
          labels: {
            formatter: function (value) {
              return value;
            },
          },
        },
        xaxis: {
          type: "datetime",
        },
        tooltip: {
          x: {
            formatter: function (value) {
              let d = new Date(value);

              return d.toLocaleString();
            },
          },
          y: {
            formatter: function (value) {
              return value;
            },
          },
        },
      };
      const ApexCharts = (await import("apexcharts")).default;
      chart = new ApexCharts(document.getElementById(id), options);
      chart.render();
      headerSelected = cardStates.data[0].timespan ?? "week";
      changeZoom(headerSelected);
    });
  }
</script>

<CardRoot
  updateSettings={(formData) => {
    console.log("apex charts submit", formData);

    // if (isEmpty(formData.get("name"))) {
    //   return { status: "fail"};
    // }
    // if (isEmpty(formData.get("data_type"))) {
    //   return { status: "fail"};
    // }
    // if (isEmpty(formData.get("beehive_id"))) {
    //   return { status: "fail"};
    // }

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
  {onDragStart}
  {onDragEnd}
  {error}
>
  <div
    slot="header"
    class="w-full max-w-[15rem] flex justify-around items-center"
  >
    {#if chart}
      {#each miniButtons as item}
        <ButtonSmall
          text={item[1]}
          type={headerSelected == item[0] ? "primary" : "secondary"}
          onClick={() => {
            headerSelected = item[0];
            changeZoom(item[0]);
          }}
        />
      {/each}
    {/if}
  </div>
  <div class="w-full max-h-full flex relative">
    <!-- {#if chartRef} -->
    <div {id} class="w-full h-full" />
    <!-- {/if} -->
    <!-- {#if chartRef} -->
    <!-- <Line bind:chart={chartRef} {options} {data} /> -->
    <!-- <h1 class="text-white">{hh} - {ww}</h1> -->
    <!-- {/if} -->
  </div>

  <div class="" slot="customSettings">
    <DropdownInput
      label="Typ dát"
      name="data_type"
      value={cardStates.data[0].type ?? "dummy"}
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
      value={cardStates.data[0].timespan ?? "week"}
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
      value={cardStates.data[0].beehive_id ?? "all"}
      small={"Váha pre ktorú sa budú zobrazovať dáta"}
      options={[["all", "all"], ...shared.getBeehiveIdsWithNames()]}
    />
  </div>
</CardRoot>
