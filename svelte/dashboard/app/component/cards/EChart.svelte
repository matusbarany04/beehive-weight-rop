<script>
    import {onMount} from "svelte";
    import * as echarts from "echarts/dist/echarts.js";
    import shared, {onLoad} from "../../stores/shared";
    import CardRoot from "./components/CardRoot.svelte";
    import {generateUUID} from "../../../../components/lib/utils/staticFuncs";
    import ButtonSmall from "../../../../components/Buttons/ButtonSmall.svelte";
    import DropdownInput from "../../../../components/Inputs/DropdownInput.svelte";
    import {tick} from "svelte"

    /**
     * @type {object}
     */
    export let cardStates;

    /**
     * @type {function}
     */
    export let onDragEnd; // function
    /**
     * @type {function}
     */
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
                console.log("element ", element)
                beehiveData.push({
                    name: element.name,
                    data: shared.getDataByType(element.type, element.beehive_id, true),
                });

                console.log(beehiveData)
            }
        });

        let initOptions = () => {
            return {
                title:{
                    show: false,
                },
                toolbox: {
                    feature: {
                        saveAsImage: {}
                    }
                },
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'cross',
                        animation: false,
                        label: {
                            backgroundColor: '#ccc',
                            borderColor: '#aaa',
                            borderWidth: 1,
                            shadowBlur: 0,
                            shadowOffsetX: 0,
                            shadowOffsetY: 0,
                            color: '#222'
                        }
                    }
                },
                grid: {
                    left: '6%',
                    right: '5%',
                    top: '4%'
                },
                xAxis: {
                    data: beehiveData[0].data.map(function (item) {
                        return item[0];
                    }),
                    boundaryGap: false
                },
                yAxis: {},
                dataZoom: [
                    {
                        startValue: "2014-06-01",
                    },
                    {
                        type: "inside",
                    },
                ],

                series: {
                    name: beehiveData[0].name,
                    type: "line",
                    data: beehiveData[0].data.map(function (item) {
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
                myChart.resize()
            })

            window.addEventListener("resize", (e) => {
                myChart.resize();
            });

        });
    }

</script>

<CardRoot>
    <!-- Svelte component structure -->
    <div id="mainss" class="w-full h-full"></div>
</CardRoot>


<CardRoot
        updateSettings={(formData) => {
    console.log("echart charts submit", formData);

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
            class="flex w-full max-w-[15rem] items-center justify-around"
    >
        {#if chart}
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
        <div {id} class="h-full w-full"/>
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
