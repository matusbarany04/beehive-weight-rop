<script>
    import { onMount } from "svelte";
    import CardRoot from "./components/CardRoot.svelte";

    export let cardStates;
    export let onDragEnd; // function
    export let onDragStart; // function
    let component = "MapCard";

    // If you're playing with this in the Svelte REPL, import the CSS using the
    // syntax in svelte:head instead. For normal development, this is better.
    import "leaflet/dist/leaflet.css";
    let map;
    let L;
    onMount(async () => {
        // Importing the leaflet library dynamically on the client-side
        const module = await import("leaflet");
        L = module.default;
    });

    function createMap(container) {
        let m = L.map(container).setView([48.7161175, 21.2527919],12.25);
        L.tileLayer(
            "https://tile.openstreetmap.org/{z}/{x}/{y}.png",
            {
                attribution: `&copy;<a href="https://www.openstreetmap.org/copyright" target="_blank">OpenStreetMap</a>,
          &copy;<a href="https://carto.com/attributions" target="_blank">CARTO</a>`,
                subdomains: "abcd",
                maxZoom: 14,
            }
        ).addTo(m);

        return m;
    }

    function mapAction(container) {
        map = createMap(container);
        return {
            destroy: () => {
                map.remove();
            },
        };
    }
    function resizeMap() {
        if (map) {
            map.invalidateSize();
        }
    }
</script>

<CardRoot {component} {cardStates} {onDragStart} {onDragEnd}>
    <!-- TODO ked myska dragguje nad mapou mapa sa hybe -->
    {#if L}
        <div class="z-10  rounded-md w-full h-full  box-border" use:mapAction on:resize={resizeMap} />
    {/if}
</CardRoot>
