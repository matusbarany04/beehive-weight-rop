<script>
  import { onMount } from "svelte";

  /**
   * @param string - value of input
   */
  export let value;

  /**
   * @param boolean - determine whether input should be focused
   */
  export let focus;

  let input, prevValue, isFocused;

  if(focus) isFocused = true;
  
  $: if (isFocused && input) input.focus();

  function keydown(e) {
    if (e.key === "Enter") focus = false;
    else if (e.key === "Escape") {
      isFocused = false;
      value = prevValue;
    }
  }

  function focusAction() {
    input.select();
    prevValue = value;
  }
</script>

{#if value}
  {#if isFocused}
    <input
      class={$$props.class}
      bind:this={input}
      bind:value
      on:focusout={() => (isFocused = false)}
      on:focus={focusAction}
      on:keydown={keydown}
    />
  {:else}
    <div class={$$props.class} on:dblclick={() => (isFocused = true)}>{value}</div>
  {/if}
{/if}
