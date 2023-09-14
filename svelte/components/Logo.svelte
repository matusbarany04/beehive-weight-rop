<script>
  export let collapsed = false;
</script>

<a
  class="font-bree-serif box-border flex h-12 w-full items-center px-4"
  href="/"
>
  <div
    class="mr-4 h-8 w-8 bg-contain bg-center bg-no-repeat"
    style="background-image: url('/img/beeman.png');"
  ></div>
  {#if !collapsed}
    <p class="text-dark-text text-xl text-slate-100">Buzzy</p>
    <p class="second text-xl text-secondary-500">Bees</p>
  {/if}
</a>

<style>
  .font-bree-serif {
    font-family: "Bree Serif", sans-serif;
  }
</style>
