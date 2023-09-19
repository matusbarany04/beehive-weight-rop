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
  <input
    class="after:bg-white"
    type="radio"
    bind:group={selection}
    id={name}
    {value}
  />
  <label for={name}>{name}</label>

  {#if selection === value}
    <slot />
  {/if}
</div>

<style>
  input[type="radio"]:checked:after {
    width: 7px;
    height: 7px;
    border-radius: 10px;
    top: -8px;
    left: 3px;
    position: relative;
    background-color: #ffa500;
    content: "";
    display: inline-block;
    visibility: visible;
  }
</style>
