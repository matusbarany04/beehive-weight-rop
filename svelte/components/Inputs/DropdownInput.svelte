<script>
  /**
   * @param {string} type - Type attribute for the select, although not used in the template. Defaults to 'text'.
   */
  export let type;

  /**
   * @param {string} label - Label to display above the select dropdown. Optional.
   */
  export let label;

  /**
   * @param {string|number} value - The current value of the select dropdown.
   */
  export let value;

  /**
   * @param {string} small - Small text to display above the select dropdown for extra information or description. Optional.
   */
  export let small;

  /**
   * @param {string} name - Name attribute for the select dropdown. Optional.
   */
  export let name;

  /**
   * @param {Array<Array<string|number>>} options - An array of options for the select dropdown. Each option is an array where the first element is the value and the second element is the display text.
   */
  export let options;

  function typeAction(node) {
    node.type = type;
  }
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
    {value}
    id="pet-select"
  >
    {#each options as option}
      <option value={option[0]}>{option[1]}</option>
    {/each}
  </select>
</div>
