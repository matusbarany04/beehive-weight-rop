<script>
  export let name;
  export let type;
  export let action;

  let sensorPorts = {
    weight: "W",
    temp: "A",
    humidity: "B",
    light: "A",
    sound: "B",
  };
</script>

<div
  class="min-h-full w-full cursor-pointer rounded bg-slate-100 p-2 text-center duration-200 hover:scale-110"
  on:click={() => action(type, name, sensorPorts[type])}
>
  <img class="m-auto w-8" src="../../../icons/{type}.svg" alt={name} />

  {#if name}
    <div>{name}</div>
  {/if}
</div>
