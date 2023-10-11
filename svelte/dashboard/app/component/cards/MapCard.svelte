<script>
  import CardRoot from "./components/CardRoot.svelte";
  import L from "leaflet";
  import { onMount } from "svelte";
  import { tick } from "svelte";
  export let cardStates;

  let component = "MapCard";
  import "leaflet/dist/leaflet.css";

  let map;

  function createMap(container) {
    let m = L.map(container).setView([48.7161175, 21.2527919], 12.25);
    L.tileLayer("https://tile.openstreetmap.org/{z}/{x}/{y}.png", {
      attribution: `&copy;<a href="https://www.openstreetmap.org/copyright" target="_blank">OpenStreetMap</a>,
          &copy;<a href="https://carto.com/attributions" target="_blank">CARTO</a>`,
      subdomains: "abcd",
      maxZoom: 14,
    }).addTo(m);

    return m;
  }

  function mapAction(container) {
    map = createMap(container);

    tick().then(() => {});

    return {
      destroy: () => {
        map.remove();
      },
    };
  }
</script>

<CardRoot {component} {cardStates}>
  {#if L}
    <div class="z-10 box-border h-full w-full rounded-md" use:mapAction />
  {/if}
</CardRoot>
