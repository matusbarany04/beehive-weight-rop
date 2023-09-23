<script>
  import Sensor from "./Sensor.svelte";
  import EditText from "../../../../components/Inputs/EditText.svelte";
  import {onMount} from "svelte";

  export let devices = {};
  export let ports;

  const WEIGHT_PORT = "W1";
  let loaded = false;
  let x;
  
  onMount(() => loaded = true);
  
  function focusInput(e) {
    e.target.select();
  }
</script>

<div class="">
  <div class="w-full">
    {#if devices[WEIGHT_PORT]}
      <EditText
        class="ml-[40%] w-1/5 text-center"
        bind:value={devices[WEIGHT_PORT]["name"]}
        focus
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
    </div>

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
    </svg>

    <div class="flex">
      {#each ports as port}
        <div class="w-full bg-transparent" draggable="true">
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
  </div>
</div>
