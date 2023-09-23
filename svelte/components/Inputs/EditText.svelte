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

  let input, prevValue;

  onMount(() => input.focus());

  $: if (focus && input) input.focus();

  function keydown(e) {
    if (e.key === "Enter") focus = false;
    else if (e.key === "Escape") {
      focus = false;
      value = prevValue;
    }
  }

  function focusAction() {
    input.select();
    prevValue = value;
  }
</script>

{#if value}
  {#if focus}
    <input
      class={$$props.class}
      bind:this={input}
      bind:value
      on:focusout={() => (focus = false)}
      on:focus={focusAction}
      on:keydown={keydown}
    />
  {:else}
    <div class={$$props.class} on:dblclick={() => (focus = true)}>{value}</div>
  {/if}
{/if}
