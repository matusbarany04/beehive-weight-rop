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
   *
   *  @param {Array<Array<string|number>>} options - An array of options for the select dropdown. Each option is an array where the first element is the value and the second element is the display text.
   *  @example
   * options={[
   *         ["10", "10 minút"],
   *         ["60", "1 hodinu"],
   *         ["240", "4 hodiny"],
   *         ["480", "8 hodín"],
   *         ["1440", "1 deň"],
   *       ]}
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
    class="mr-4 h-8 w-72 rounded-md border-2 border-slate-300 bg-white px-4"
    {name}
    {value}
    id="pet-select"
  >
    {#each options as option}
      <option value={option[0]}>{option[1]}</option>
    {/each}
  </select>
</div>
