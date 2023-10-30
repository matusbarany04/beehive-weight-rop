<script>
  import { TW_BREAKPOINTS } from "../../../../components/lib/utils/staticFuncs";
  import { writable } from "svelte/store";
  import panelState from "./panelState";

  export let retracable = true;

  let visible = panelState.isOpened();

  panelState.getOpenedRef().subscribe((visibility) => {
    visible = visibility;
  });

  let absolute = true;
  let screenWidth = 9999;

  function toggleVisibility() {
    panelState.toggleOpened();
  }

  let previousWidth = window.innerWidth;
  $: {
    // If current width is below the threshold and previous width was above it
    if (screenWidth < TW_BREAKPOINTS.lg && previousWidth >= TW_BREAKPOINTS.lg) {
      panelState.setOpened(false);
    }
    // If current width is above the threshold and previous width was below it
    else if (
      screenWidth >= TW_BREAKPOINTS.lg &&
      previousWidth < TW_BREAKPOINTS.lg
    ) {
      panelState.setOpened(true);
    }
    previousWidth = screenWidth;
  }
</script>

<svelte:window bind:innerWidth={screenWidth} />

<div class="relative z-50 no-scrollbar">
  <section
    class="{visible
      ? 'overflow-x-visible'
      : 'overflow-x-hidden'} animate-width absolute h-screen flex-col bg-primary-100 no-scrollbar lg:relative {visible
      ? 'w-56'
      : 'w-0'}"
  >
    {#if retracable}
      <button
        class="absolute right-2 top-4 rounded-full bg-secondary-500 p-1 transition-all duration-100"
        on:click={toggleVisibility}
      >
        <div
          class="m-auto h-4 w-4 bg-contain bg-no-repeat"
          style="background-image: url(/icons/caret-left-fill.svg)"
        ></div>
      </button>
    {/if}
    <slot />
  </section>
</div>
{#if !visible && retracable}
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
