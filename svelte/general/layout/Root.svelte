<script>
  import NavigationBar from "../app/components/NavigationBar.svelte";

  export let className = "";

  export let title = null;
</script>

<main
  class="{className} min-h-screen w-screen overflow-y-scroll bg-primary-100"
>
  <slot name="beforeNavBar" />
  <NavigationBar />
  {#if title}
    <div class="min-h-24 w-full">
      <h1 class="m-auto text-center text-7xl font-bold text-tertiary-100">
        {title}
      </h1>
    </div>
  {/if}
  <div class="p-4">
    <slot />
  </div>
</main>
