<script>
  export let title;
</script>

<section class="h-5 px-6 py-0">
  <h1 class="font-medium text-slate-300">{title}</h1>
  <div class="px-6 py-0">
    <slot />
  </div>
</section>
