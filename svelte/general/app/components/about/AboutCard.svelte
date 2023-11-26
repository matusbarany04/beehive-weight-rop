<script>
  export let className = "";

  export let image = "";

  /**
   * can be 'right' or 'left'
   */
  export let direction = "right";

  export let name = "";
  export let description = "";
</script>

<div
  class="{className}  flex {direction === 'left'
    ? 'flex-col md:flex-row'
    : 'flex-col md:flex-row-reverse'}  mx-auto my-4 min-h-min max-w-4xl rounded-md md:h-64"
>
  <div
    class="mx-auto aspect-square w-5/6 rounded-full border border-tertiary-100 bg-contain bg-center bg-no-repeat md:mx-5 md:h-full md:w-min"
    style="background-image: url({image})"
  ></div>

  <div
    class="flex-1 p-4 text-slate-100 {direction === 'left'
      ? 'text-start'
      : 'text-end'}"
  >
    <!-- name -->
    <h1 class="mb-4 text-3xl font-bold">{name}</h1>
    <p class="text">{description}</p>
  </div>
</div>
