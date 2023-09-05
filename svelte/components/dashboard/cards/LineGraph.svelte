<script>
    import CardRoot from "./components/CardRoot.svelte";


    import {generateUUID} from "../../lib/utils/static";
    import {getContext, onMount} from "svelte";

    export let cardStates;
    export let onDragEnd; // function
    export let onDragStart; // function
    // export let beehiveData;
    // export let beehiveDataType;
    let component = "LineGraph";

    let id = generateUUID();


    import Chart from 'chart.js/auto'

    let chartRef;
    let options;
    let data;
    onMount(async () => {
        const {default: zoomPlugin} = await import("chartjs-plugin-zoom");


        Chart.register(
            zoomPlugin
        );

        const zoomOptions = {
            pan: {
                enabled: true,
                mode: "x",
            },
            zoom: {
                wheel: {
                    enabled: true,
                },
                pinch: {
                    enabled: true,
                },
                mode: "x",
            },
        };

        options = {
            maintainAspectRatio: false,
            responsive: true,
            animation: false,
            // responsive: true,
            plugins: {
                legend: {
                    position: "top",
                },
                title: {
                    display: true,
                    text: "Chart.js Line Chart",
                },
                zoom: zoomOptions,
            },
        };
        let beehiveData;

        const labels = [];
        let temps = []
        beehiveData.forEach(element => {
            temps.push(element.temperature);
            labels.push(new Date(element.timestamp).toLocaleString());
        });
        data = {
            labels,
            datasets: [
                {
                    label: "Dataset 1",
                    data: temps,
                    borderColor: "rgb(255, 99, 132)",
                    backgroundColor: "rgba(255, 99, 132, 0.5)",
                },
                // {
                //     label: "Dataset 2",
                //     data: [2, 3, 1, 3, 6, 3, 2, 2, 4, 5],
                //     borderColor: "rgb(53, 162, 235)",
                //     backgroundColor: "rgba(53, 162, 235, 0.5)",
                // },
            ],
        };

        chartRef = new Chart(id, {
            type: "line",
            data: data,
            options: options,
        });
    });
</script>

<CardRoot
        {component}
        title={"Váha úľov"}
        {cardStates}
        {onDragStart}
        {onDragEnd}
>
    <div class="w-full max-h-full flex relative">
        <canvas {id} class="w-full h-full"/>
    </div>
</CardRoot>
