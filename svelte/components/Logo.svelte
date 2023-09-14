<script>
    export let collapsed = false;
</script>

<a class="px-4 box-border flex items-center h-12 w-full font-bree-serif" href="/">
    <div class="w-8 h-8 mr-4 bg-center bg-no-repeat bg-contain" style="background-image: url('/img/beeman.png');"></div>
    {#if !collapsed}
        <p class="text-dark-text  text-xl text-slate-100">Buzzy</p>
        <p class="second text-xl text-secondary-500">Bees</p>
    {/if}
</a>

<style>
    .font-bree-serif {
        font-family: 'Bree Serif', sans-serif;
    }
</style>