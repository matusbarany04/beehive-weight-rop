<script>
  export let title = "";

  export let description = "";

  export let className = "";
  export let bgClass = "bg-white";
</script>

<div
  class="{className} min-h-16 mx-auto mb-4 rounded-lg shadow shadow-tertiary-300 {bgClass} p-4 lg:w-5/6"
>
  <!-- title -->
  <h1 class="text-2xl font-semibold">{title}</h1>
  <h1 class="text-base font-normal">{description}</h1>
</div>
