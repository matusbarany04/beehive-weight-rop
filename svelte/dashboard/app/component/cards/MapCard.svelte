<script>
  import CardRoot from "./components/CardRoot.svelte";
  import L from "leaflet";
  import { onMount } from "svelte";
  import { tick } from "svelte";

  export let cardStates;

  let component = "MapCard";
  import "leaflet/dist/leaflet.css";

  /**
   * Creates a Leaflet map and initializes it with a base tile layer.
   *
   * @param {string|HTMLElement} container - The HTML element or ID of the container for the map.
   * @returns {L.Map} The created Leaflet map instance.
   */
  function createMap(container) {
    let m = L.map(container);

    m.setView([48.7161175, 21.2527919], 12.25);
    L.tileLayer("https://tile.openstreetmap.org/{z}/{x}/{y}.png", {
      attribution: `&copy;<a href="https://www.openstreetmap.org/copyright" target="_blank">OpenStreetMap</a>,
          &copy;<a href="https://carto.com/attributions" target="_blank">CARTO</a>`,
      subdomains: "abcd",
      maxZoom: 14,
    }).addTo(m);

    return m;
  }

  /**
   * Initializes a Leaflet map and performs necessary actions on it.
   *
   * @param {string|HTMLElement} container - The HTML element or ID of the container for the map.
   * @returns {Object} An object with a `destroy` function for cleaning up the map.
   */
  function mapAction(container) {
    /** @type {L.map} */
    const map = createMap(container);

    /** this is a workaround for map not fully loading, if this is not present, map loads only after window resize */
    tick().then(() => {
      setTimeout(() => {
        map.invalidateSize();
      }, 300);
    });

    return {
      /**
       * Destroys the Leaflet map and associated resources.
       */
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
