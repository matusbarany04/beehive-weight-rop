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

  /**
   * @param {boolean} inline - If set, the label will be on the left and the input on the right. Optional.
   */
  export let inline = false;

  export let className = "";

  /**
   * @param value
   */
  export let onChange = (value) => {
    console.warn("change not implemented! " + value);
  };

  function typeAction(node) {
    node.type = type;
  }
</script>

<div class={(inline ? "flex items-center gap-2" : "") + " mb-4 " + className}>
  {#if label}
    <label for={name} class={inline ? "w-1/3" : ""}>{label}</label> <br />
  {/if}
  {#if small}
    <small>{small}</small><br />
  {/if}
  <select
    class="h-8 w-full rounded-md border-2 border-slate-300 bg-white px-4"
    on:change={(event) => onChange(event.target.value)}
    {name}
    {value}
    id="pet-select"
  >
    {#each options as option}
      <option value={option[0]}>{option[1]}</option>
    {/each}
  </select>
</div>
