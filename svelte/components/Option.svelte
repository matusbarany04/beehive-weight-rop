<script>
  import RouterLink from "./RouterLink.svelte";
  export let url;
  
</script>
<RouterLink url="/beehives/add{url}">
  <div class="flex items-center rounded-lg bg-white p-5 mt-2 hover:bg-slate-300 duration-200">
    <div class="w-full">
      <slot></slot>
    </div>
    <img src="/icons/arrow-right.svg" alt="select">
</div>

</RouterLink>