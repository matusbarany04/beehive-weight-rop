<script>
  export let detail;
  export let title;

  export let className = "";
  export let detailClassName = "";
  export let titleClassName = "";
</script>

<div
  class="{className} min-h-48 lg:min-h-24 mx-auto mb-4 flex flex-col justify-between rounded-lg bg-white p-4 shadow shadow-tertiary-300 lg:w-5/6 lg:flex-row lg:justify-center"
>
  <!-- title -->
  <div class="flex-1 justify-center lg:justify-normal">
    {#if title}
      <h1 class="{titleClassName} text-lg font-semibold">{title}</h1>
    {/if}
    {#if detail}
      <h4 class="{detailClassName} text-base font-normal">
        {detail}
      </h4>
    {/if}
  </div>
  <div class=" flex max-w-sm flex-1 items-center lg:justify-end">
    <slot><h1>práve pripravujeme!</h1></slot>
  </div>
</div>
