<script>
  import NavigationBar from "../app/components/NavigationBar.svelte";

  export let className = "";

  export let style = "";

  export let title = null;
</script>

<!--overflow-x-hidden overflow-y-scroll-->
<main
  {style}
  class="{className} max-w-screen flex min-h-screen flex-col overflow-x-hidden bg-primary-500"
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
