<script>

  export let name;
  export let value;
  export let selection;

</script>

<div class="p-4 rounded-lg duration-200 {selection === value ? 'bg-slate-100' : 'hover:text-slate-500'}" on:click={() => selection = value}>

  <input class="after:bg-white" type="radio" bind:group={selection} id={name} value="{value}">
  <label for={name}>{name}</label>

  {#if selection === value}
    <slot></slot>
  {/if}

</div>
<style>
  input[type='radio']:checked:after {
    width: 7px;
    height: 7px;
    border-radius: 10px;
    top: -8px;
    left: 3px;
    position: relative;
    background-color: #ffa500;
    content: '';
    display: inline-block;
    visibility: visible;
  }
</style>