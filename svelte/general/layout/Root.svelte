<script>
  import NavigationBar from "../app/components/NavigationBar.svelte";

  export let className = "";

  export let style = "";

  export let title = null;
</script>

<main
  {style}
  class="{className} flex min-h-screen w-screen flex-col overflow-y-scroll bg-primary-500"
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
  <div class="relative flex min-h-full flex-1 flex-col">
    <slot />
  </div>
</main>
