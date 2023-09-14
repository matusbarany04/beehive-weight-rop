<script>
  import TW_BREAKPOINTS from "../../lib/utils/static";

  let visible = true;
  let absolute = true;
  let screenWidth = 9999;

  function toggleVisibility() {
    visible = !visible;
  }

  let previousWidth = window.innerWidth;
  $: {
    // If current width is below the threshold and previous width was above it
    if (screenWidth < TW_BREAKPOINTS.lg && previousWidth >= TW_BREAKPOINTS.lg) {
      visible = false;
    }
    // If current width is above the threshold and previous width was below it
    else if (
      screenWidth >= TW_BREAKPOINTS.lg &&
      previousWidth < TW_BREAKPOINTS.lg
    ) {
      visible = true;
    }
    previousWidth = screenWidth;
  }
</script>

<svelte:window bind:innerWidth={screenWidth} />

<div class="relative z-50">
  <section
    class="{visible
      ? ''
      : 'overflow-x-hidden'} animate-width absolute h-screen flex-col bg-primary-100 lg:relative {visible
      ? 'w-56'
      : 'w-0'}"
  >
    <button
      class="absolute right-2 top-4 rounded-full bg-secondary-500 p-1 transition-all duration-100"
      on:click={toggleVisibility}
    >
      <div
        class="m-auto h-4 w-4 bg-contain bg-no-repeat"
        style="background-image: url(/icons/caret-left-fill.svg)"
      ></div>
    </button>
    <slot />
  </section>
</div>
{#if !visible}
  <button
    class="absolute left-4 top-4 rounded-full bg-primary-100 p-2 transition-all duration-100 hover:bg-secondary-500"
    on:click={toggleVisibility}
  >
    <div
      class="m-auto h-4 w-4 bg-contain bg-no-repeat"
      style="background-image: url(/icons/dashboard.svg)"
    ></div>
  </button>
{/if}

<style>
  .animate-width {
    transition: width 0.3s ease;
  }
</style>
