<script>
  //here are exported props
  export let type;
  export let label;
  export let value;
  export let small;
  export let name;
  export let options;

  function typeAction(node) {
    node.type = type;
  } //this function helps to define type of input
</script>

<div>
  {#if label}
    <label for={name} class="">{label}</label> <br />
  {/if}
  {#if small}
    <small>{small}</small><br />
  {/if}
  <select
    class="px-4 mr-4 w-72 h-8 rounded-md border-2 bg-white border-slate-300"
    {name}
    value="{value}"
    id="pet-select"
  >
    {#each options as option}
      <option value={option[0]}>{option[1]}</option>
    {/each}
  </select>
</div>
