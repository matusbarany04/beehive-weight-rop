<script>
  export let detail;
  export let title;
  export let inputPlaceholder;
</script>

<div
  class="mx-auto mb-4 flex h-48 flex-col justify-between rounded-lg bg-white p-4 lg:h-24 lg:w-5/6 lg:flex-row lg:justify-center"
>
  <!-- title -->
  <div class="flex-1 justify-center lg:justify-normal">
    <h1 class="text-lg font-semibold">{title}</h1>
    <h4 class="text-base font-normal">
      {detail}
    </h4>
  </div>
  <div class=" flex max-w-sm flex-1 items-center lg:justify-end">
    <slot><h1>práve pripravujeme!</h1></slot>
  </div>
</div>
