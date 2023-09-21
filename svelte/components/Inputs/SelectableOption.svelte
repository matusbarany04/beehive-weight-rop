<script>
  /**
   * @param {string} name - Title of the option.
   */
  export let name;

  /**
   * @param {string} value - Value which will be given to a form.
   */
  export let value;

  /**
   * @param {string} selection - Group variable to store the current value.
   */
  export let selection;
</script>

<div
  class="rounded-lg p-4 duration-200 {selection === value
    ? 'bg-slate-100'
    : 'hover:text-slate-500'}"
  on:click={() => (selection = value)}
>
  <div class="flex gap-2 items-center">
    <input
      class="appearance-none w-4 h-4 rounded-full border border-black bg-clip-content bg-white transition duration-100 checked:bg-secondary-500 checked:p-[2px]"
      type="radio"
      bind:group={selection}
      id={name}
      {value}
    />
    <label for={name}>{name}</label>
  </div>

  {#if selection === value}
    <slot/>
  {/if}
</div>

<style>


</style>
