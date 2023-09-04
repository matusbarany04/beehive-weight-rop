<script>
  //here are exported props
  export let type;
  export let label;
  export let placeholder;
  export let value;
  export let small;
  export let name;
  export let required;

  function typeAction(node) {
    node.type = type;
  } //this function helps to define type of input
</script>

<!-- 
<div class="myInput">
  <label class="myLabel"
    >{label}
    <input on:input bind:value type="text" {placeholder} use:typeAction />
  </label>
</div> -->
<div>
  {#if label}
    <label for={name} class="">{label}</label> <br />
  {/if}
  {#if small}
    <small>{small}</small><br />
  {/if}

  <input
    on:input
    {value}
    type="text"
    {placeholder}
    use:typeAction
    {name}
    class="h-8 w-full sm:w-96 pl-4 mt-1 mb-2 rounded-md border-2 border-slate-300"
    {required}
  />
</div>
