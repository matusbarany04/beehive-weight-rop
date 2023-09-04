<script>
    import { onMount } from "svelte";
    import CardRoot from "./components/CardRoot.svelte";
    import { modes, getDefaultMode } from "./components/cardConfig";

    export let cardStates;
    export let onDragEnd; // function
    export let onDragStart; // function
    let component = "WeatherCard";
</script>

<CardRoot {component} {cardStates} {onDragStart} {onDragEnd}>
    <!-- TODO ked myska dragguje nad mapou mapa sa hybe -->

    <iframe
        title="weather"
        class="w-full h-full"
        src="https://embed.windy.com/embed2.html?lat=49.724&lon=22.720&detailLat=48.678&detailLon=21.267&width=650&height=450&zoom=5&level=surface&overlay=rain&product=ecmwf&menu=&message=true&marker=&calendar=now&pressure=&type=map&location=coordinates&detail=&metricWind=km%2Fh&metricTemp=%C2%B0C&radarRange=-1"
        frameborder="0"
    />
</CardRoot>
