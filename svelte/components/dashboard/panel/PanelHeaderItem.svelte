<script>
  export let title;
</script>

<section class="px-6 py-0 h-5">
  <h1 class="text-slate-300 font-medium">{title}</h1>
  <div class="px-6 py-0">
    <slot />
  </div>
</section>
