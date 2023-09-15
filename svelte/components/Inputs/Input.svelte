<script>
  /**
   * @param {string} type - Specifies the type of the input element (e.g., 'text', 'password'). Defaults to 'text'.
   */
  export let type;

  /**
   * @param {string} label - Label to display above the input. Optional.
   */
  export let label;

  /**
   * @param {string} placeholder - Placeholder text for the input. Optional.
   */
  export let placeholder;

  /**
   * @param {string|number} value - Current value of the input.
   */
  export let value;

  /**
   * @param {string} small - Small text to display above the input for extra information or description. Optional.
   */
  export let small;

  /**
   * @param {string} name - Name attribute for the input. Optional.
   */
  export let name;

  /**
   * @param {boolean} required - If set, the input will be required. Optional.
   */
  export let required;

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

  <input
    on:input
    {value}
    type="text"
    {placeholder}
    use:typeAction
    {name}
    class="mb-2 mt-1 h-8 w-full rounded-md border-2 border-slate-300 pl-4 sm:w-96"
    {required}
  />
</div>
