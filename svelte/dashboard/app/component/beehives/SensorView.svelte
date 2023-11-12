<script>
  import Sensor from "./Sensor.svelte";
  import EditText from "../../../../components/Inputs/EditText.svelte";
  import { onMount } from "svelte";

  export let devices = {};

  let portRects = {
    S1: "g37609",
    S2: "g37609-3",
    S3: "g37609-8",
    S4: "g37609-0",
    S5: "g37609-0-8",
    S6: "g37609-0-1",
  };

  const FIRST_ROW = ["S1", "S2", "S3"];
  const SECOND_ROW = ["S4", "S5", "S6"];

  const WIRE_WIDTH = 20;
  const WIRE_LENGTH = 1000;
  const CONNECTOR_COLOR = "black";
  let loaded = false;
  let x;

  onMount(() => (loaded = true));

  console.log(SVGRectElement.prototype);

  function focusInput(e) {
    e.target.select();
  }

  function incrementAttr(rect, attr, value) {
    rect.setAttribute(attr, parseInt(rect.getAttribute(attr)) + value);
  }

  function render(svgDocument) {
    let index = 0;
    for (let port in devices) {
      const device = devices[port];
      console.log(port);
      const g = svgDocument.getElementById(portRects[port]);
      g.removeChild(g.getElementsByTagName("g")[0]);
      const rect = g.children[0];
      rect.style.fill = CONNECTOR_COLOR;
      incrementAttr(rect, "width", 60);
      incrementAttr(rect, "height", 60);
      incrementAttr(rect, "x", -30);
      incrementAttr(rect, "y", -30);
      addSensor(svgDocument, g, rect, index);
      index++;
    }
  }

  function addSensor(svgDocument, g, rect, index) {
    const wire = svgDocument.createElementNS(
      "http://www.w3.org/2000/svg",
      "rect",
    );
    const rectY = parseInt(rect.getAttribute("y"));
    const wireY =
      index <= 2
        ? rectY - WIRE_LENGTH
        : rectY + parseInt(rect.getAttribute("height"));
    wire.setAttribute(
      "x",
      parseInt(rect.getAttribute("x")) +
        parseInt(rect.getAttribute("width")) / 2 -
        WIRE_WIDTH / 2,
    );
    wire.setAttribute("y", wireY);
    wire.setAttribute("height", WIRE_LENGTH);
    wire.setAttribute("width", WIRE_WIDTH);
    wire.setAttribute("style", "fill: " + CONNECTOR_COLOR);
    g.appendChild(wire);
  }

  function customizeSize(e) {
    const svg = e.target.contentDocument.children[0];
    console.log(svg);
    svg.setAttribute("viewBox", "0 0 3000 1000");
    render(e.target.contentDocument);
  }
</script>

<div class="">
  <!-- <div class="w-full">
    {#if devices[WEIGHT_PORT]}
      <EditText
        class="ml-[40%] w-1/5 text-center"
        bind:value={devices[WEIGHT_PORT]["name"]}
        focus={loaded}
      />
    {/if}
    <div class=" m-auto h-14 w-14 rounded">
      {#if devices[WEIGHT_PORT]}
        <Sensor type={devices[WEIGHT_PORT]["type"]} />
      {/if}
    </div>
  </div>
  <div class="relative m-auto w-4/5">
    <div class="absolute top-12 w-full text-center">
      <div>{WEIGHT_PORT}</div>
      <div class="flex w-full justify-between">
        {#each ports as port}
          <div class="w-full text-center">{port}</div>
        {/each}
      </div>
    </div>-->

  <div class="ml-[10%] flex w-[36%] pl-[3%]">
    {#each FIRST_ROW as port}
      <div class="w-full bg-transparent">
        <div class=" m-auto h-14 w-14 rounded">
          {#if devices[port]}
            <Sensor type={devices[port]["type"]} />
          {/if}
        </div>
        {#if devices[port]}
          <EditText
            class="w-full text-center"
            bind:value={devices[port]["name"]}
            focus={loaded}
          />
        {/if}
      </div>
    {/each}
  </div>

  <object
    class="ml-[10%] w-3/5"
    id="sensorView"
    type="image/svg+xml"
    data="../../../img/panel.svg"
    on:load={customizeSize}
  ></object>

  <div class="ml-[12%] flex w-2/5">
    {#each SECOND_ROW as port}
      <div class="w-full bg-transparent">
        <div class=" m-auto h-14 w-14 rounded">
          {#if devices[port]}
            <Sensor type={devices[port]["type"]} />
          {/if}
        </div>
        {#if devices[port]}
          <EditText
            class="w-full text-center"
            bind:value={devices[port]["name"]}
            focus={loaded}
          />
        {/if}
      </div>
    {/each}
  </div>

  <!--
    <svg height="150" width="100%">
      <rect
        x="0"
        y="50"
        width="100%"
        height="50"
        class="fill-none stroke-slate-700 stroke-2"
      />
      <line
        x1="50%"
        x2="50%"
        y1="0"
        y2="50"
        class="stroke-slate-700 stroke-2"
      />
      {#each ports as port, index}
        {(x = (100 / ports.length) * (index + 0.5))}
        <line
          x1="{x}%"
          x2="{x}%"
          y1="100"
          y2="150"
          class="stroke-slate-700 stroke-2"
        />
      {/each}
    </svg>-->
</div>
