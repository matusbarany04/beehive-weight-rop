<script>
    import CardRoot from "./components/CardRoot.svelte";

    import Chart from "chart.js/auto";
    import { generateUUID } from "../lib/utils/static";
    import { getContext, onMount } from "svelte";

    export let cardStates;
    export let onDragEnd; // function
    export let onDragStart; // function
    let component = "DoughnutGraph";

    let id = generateUUID();

    const chartData = [
        { year: 2010, count: 10 },
        { year: 2011, count: 20 },
        { year: 2012, count: 15 },
        { year: 2013, count: 25 },
        { year: 2014, count: 22 },
        { year: 2015, count: 30 },
        { year: 2016, count: 28 },
    ];

    function initChart() {
        new Chart(document.getElementById(id), {
            type: "doughnut",
            options: {
                animation: false,
                responsive: true,
                maintainAspectRatio:false,
                plugins: {
                    legend: {
                        display: false,
                        position: "left",
                    },
                    title: {
                        display: false,
                        text: "Chart.js Doughnut Chart",
                    },
                },
            },
            data: {
                labels: chartData.map((row) => row.year),
                datasets: [
                    {
                        label: "Acquisitions by year",
                        data: chartData.map((row) => row.count),
                    },
                ],
            },
        });
    }

    onMount(async () => {
        initChart();
    });
</script>

<CardRoot
    {component}
    {cardStates}
    {onDragStart}
    {onDragEnd}
>
    <div class="w-full max-h-full lg:p-4">
        <canvas {id} class="" /> 
    </div>
</CardRoot>
