<script>
  export let className = "";

  export let image = "";

  /**
   * can be 'right' or 'left'
   */
  export let direction = "right";

  export let name = "";
  export let description =  "";
</script>

<div
  class="{className}  flex {direction === 'left'
    ? 'flex-row'
    : 'flex-row-reverse'} mx-auto my-4 h-64 max-w-4xl rounded-md"
>
  <div
    class="mx-5 aspect-square h-full rounded-full border border-tertiary-100 bg-contain bg-center bg-no-repeat"
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
