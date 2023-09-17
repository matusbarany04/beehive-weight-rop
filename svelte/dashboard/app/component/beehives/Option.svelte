<script>
  import RouterLink from "../../../../components/RouterLink.svelte";
  export let url;
</script>

<RouterLink url="/beehives/add{url}">
  <div
    class="mt-2 flex items-center rounded-lg bg-white p-5 duration-200 hover:bg-slate-300"
  >
    <div class="w-full">
      <slot />
    </div>
    <img src="/icons/arrow-right.svg" alt="select" />
  </div>
</RouterLink>
