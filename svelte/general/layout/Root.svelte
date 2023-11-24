<script>
  import NavigationBar from "../app/components/NavigationBar.svelte";

  export let className = "";
</script>

<main
  class="{className} min-h-screen w-screen overflow-y-scroll bg-primary-100"
>
  <slot name="beforeNavBar" />
  <NavigationBar />
  <div class="p-4">
    <slot />
  </div>
</main>
