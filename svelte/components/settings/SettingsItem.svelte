<script>
  export let detail;
  export let title;
  export let inputPlaceholder;
</script>

<div
  class="flex-col mb-4 p-4 mx-auto h-48 flex justify-between bg-white rounded-lg lg:w-5/6 lg:h-24 lg:flex-row lg:justify-center"
>
  <!-- title -->
  <div class="flex-1 justify-center lg:justify-normal">
    <h1 class="text-lg font-semibold">{title}</h1>
    <h4 class="text-base font-normal">
      {detail}
    </h4>
  </div>
  <div class=" flex-1 flex items-center lg:justify-end max-w-sm">
    <slot><h1>práve pripravujeme!</h1></slot>
  </div>
</div>
