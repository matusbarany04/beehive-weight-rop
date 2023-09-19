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

  /**
   * @param {boolean} inline - If set, the label will be on the left and the input on the right. Optional.
   */
  export let inline = false;

  function typeAction(node) {
    node.type = type;
  }
</script>

<div class={inline ? "flex items-center gap-2" : ""}>
  {#if label}
    <label for={name} class={inline ? "w-1/3" : ""}>{label}</label> <br />
  {/if}
  {#if small}
    <small>{small}</small><br />
  {/if}

  <input
    on:input
    {value}
    id={name}
    type="text"
    {placeholder}
    use:typeAction
    {name}
    class="mb-2 mt-1 h-8 w-full rounded-md border-2 border-slate-300 pl-4"
    {required}
  />
</div>
