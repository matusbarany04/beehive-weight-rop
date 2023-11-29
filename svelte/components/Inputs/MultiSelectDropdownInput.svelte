<script>
  import { onMount, tick } from "svelte";
  import ButtonSmall from "../Buttons/ButtonSmall.svelte";
  import { clickOutside } from "../lib/clickOutside";
  import { BeehiveObj } from "../../dashboard/app/stores/Beehive";
  import shared from "../../dashboard/app/stores/shared";

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

  export let default_option;

  /**
   * @param {boolean} inline - If set, the label will be on the left and the input on the right. Optional.
   */
  export let inline = false;

  export let className = "";

  /**
   * @param newValue
   */
  export let onChange = (newValue) => {
    console.warn("change not implemented! " + newValue);
  };

  let innerOnChange = () => {
    tick().then(() => {
      // changing value
      value = Object.keys(selectValue);
      onChange(Object.keys(selectValue));
    });
  };

  let selectValue = {};

  if (Array.isArray(value)) {
    for (const val of value) {
      if (val === "all") {
        selectValue[val] = "all";
      } else {
        selectValue[val] = shared.getBeehiveById(val)?.name;
      }
    }
  } else if (value == null) {
    selectValue[default_option[0]] = default_option[1];
  } else {
    selectValue[value] = value;
  }

  // check if id is not in "value" array if not add to option list
  let optionList = {};
  for (const optionListKey of options) {
    optionList[optionListKey[0]] = optionListKey[1];
  }

  if (options.length === 0) {
    console.log("adding default option");
    optionList[default_option[0]] = default_option[1];
  }

  for (const key of value) {
    if (optionList.hasOwnProperty(key)) {
      delete optionList[key];
    }
  }

  function typeAction(node) {
    node.type = type;
  }

  let focused = false;

  function handleClickOutside(event) {
    focused = false;
  }

  function clickInside(event) {
    focused = true;
    if (Object.entries(optionList).length === 0) {
      focused = false;
    }
  }

  $: {
    // if (Object.entries(optionList).length > 0){
    //   optionList[default_option[0]] = default_option[1];
    // }

    if (Object.keys(selectValue).length === 0) {
      selectValue[default_option[0]] = default_option[1];
    }

    if (Object.keys(selectValue).length === 0) {
      optionList[default_option[0]] = default_option[1];
    }

    if (Object.keys(selectValue).length > 1) {
      delete selectValue[default_option[0]];
      delete optionList[default_option[0]];
    }

    if (Object.keys(optionList).length < options.length) {
      delete optionList[default_option[0]];
    }
  }
</script>

<div class={(inline ? "flex items-center gap-2" : "") + " mb-4 " + className}>
  {#if label}
    <label for={name} class={inline ? "w-1/3" : ""}>{label}</label> <br />
  {/if}
  {#if small}
    <small>{small}</small><br />
  {/if}

  <!-- svelte-ignore a11y-no-static-element-interactions -->
  <!-- svelte-ignore a11y-click-events-have-key-events -->
  <div
    class="relative"
    on:click={clickInside}
    use:clickOutside
    on:click_outside={handleClickOutside}
  >
    <div
      class="relative block min-h-[32px] w-full rounded-md border-2 border-slate-300 bg-white px-4"
    >
      {#each Object.entries(selectValue) as [param, displayValue] (param)}
        <!-- svelte-ignore a11y-no-static-element-interactions -->
        <div class="m-1 inline-block" key={param}>
          <!-- svelte-ignore a11y-click-events-have-key-events -->
          <!-- svelte-ignore a11y-no-static-element-interactions -->
          <div
            class="root inline-block h-min w-min cursor-pointer rounded-lg px-2 btn-secondary"
            on:click={() => {
              if (param !== default_option[0]) {
                delete selectValue[param];
                optionList[param] = displayValue;

                innerOnChange();
              }
            }}
          >
            <p class="text no_wrap text-ellipsis whitespace-nowrap text-sm">
              {displayValue}
            </p>
          </div>
        </div>
      {/each}
      {#if focused}
        <div
          class="absolute -bottom-[6.25rem] left-0 z-50 h-24 w-full overflow-y-scroll rounded-md border-2 border-slate-300 bg-white px-4"
          on:change={(event) => innerOnChange()}
          {name}
          id="pet-select"
        >
          {#each Object.entries(optionList) as [optionKey, optionValue]}
            <option
              on:click={() => {
                selectValue[optionKey] = optionValue;
                delete optionList[optionKey];
                optionList = optionList;
                innerOnChange();
              }}
              value={optionKey}
              >{optionList[optionKey]}
            </option>
          {/each}
        </div>
      {/if}
    </div>
  </div>
  <select id="multipleSelect" multiple {name} {value} class="invisible h-0 w-0">
    {#each options as option}
      <option value={option[0]}>{option[1]}</option>
    {/each}
    <option value={default_option[0]}>{default_option[1]}</option>
  </select>
</div>
