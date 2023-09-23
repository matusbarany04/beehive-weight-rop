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
   * @param {Array} results - display results under the input
   */
  export let results;

  /**
   * @param {boolean} required - If set, the input will be required. Optional.
   */
  export let required;

  /**
   * @param {boolean} inline - If set, the label will be on the left and the input on the right. Optional.
   */
  export let inline = false;

  /**
   * @param {boolean} autocomplete - If set, the input will allow autocompleting saved data.
   */
  export let autocomplete = false;

  let selectedItem;

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

  <div class="relative w-full">
    <input
      on:input
      bind:value
      id={name}
      type="text"
      {placeholder}
      use:typeAction
      on:focusout={() => (results = undefined)}
      {name}
      class="mb-2 mt-1 h-8 w-full rounded-md border-2 border-slate-300 pl-4"
      {required}
      autocomplete={autocomplete ? "on" : "one-time-code"}
    />

    {#if results}
      <ul class="absolute z-40 w-full rounded bg-white shadow-lg">
        {#each results as result}
          <li
            class="cursor-pointer rounded p-1 hover:bg-slate-100"
            on:mousedown={() => (value = result)}
          >
            {result}
          </li>
        {/each}
        {#if results.length === 0}
          <li class="p-1 italic opacity-70">Žiadne výsledky</li>
        {/if}
      </ul>
    {/if}
  </div>
</div>
