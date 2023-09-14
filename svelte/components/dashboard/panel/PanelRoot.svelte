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
    else if (screenWidth >= TW_BREAKPOINTS.lg && previousWidth < TW_BREAKPOINTS.lg) {
      visible = true;
    }
    previousWidth = screenWidth;
  }

</script>

<style>
  .animate-width {
    transition: width 0.3s ease;
  }
</style>

<svelte:window bind:innerWidth="{screenWidth}"/>

<div class="relative z-50">
  <section
    class="{visible ? '' : 'overflow-x-hidden'} flex-col h-screen bg-primary-100 absolute lg:relative animate-width {(visible ? 'w-56' : 'w-0')}"
  >
    <button
      class="p-1 absolute rounded-full transition-all duration-100 bg-secondary-500 right-2 top-4"
      on:click={toggleVisibility}>
      <div
        class="m-auto bg-contain bg-no-repeat w-4 h-4"
        style="background-image: url(/icons/caret-left-fill.svg)"></div>
    </button>
    <slot></slot>
  </section>
</div>
{#if !visible}
  <button
    class="p-2 absolute rounded-full transition-all duration-100 bg-primary-100 hover:bg-secondary-500 left-4 top-4"
    on:click={toggleVisibility}
  >
    <div
      class="m-auto bg-contain bg-no-repeat w-4 h-4"
      style="background-image: url(/icons/dashboard.svg)"
    ></div>
  </button>
{/if}