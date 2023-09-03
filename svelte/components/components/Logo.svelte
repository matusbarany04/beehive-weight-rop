<script>
    export let collapsed = false;
</script>

<a class="logo" href="/" style="font-family: Bree Serif;">
    <div class="logoImg" />
    {#if !collapsed}
        <p class="logoText first">Buzzy</p>
        <p class="logoText second">Bees</p>
    {/if}
</a>

<style>
    .logo {
        display: flex;
        height: 50px;
        width: 100%;
        align-items: center;
        justify-content: center;
    }

    .logoImg {
        display: inline-block;
        height: 80%;
        aspect-ratio: 1/1;
        background-image: url("/beeman.png");
        background-position: center;
        background-size: contain;
        background-repeat: no-repeat;
        box-sizing: border-box;
        margin: auto 0;
        margin-right: 5px;
    }
    .logoText {
        display: inline;
        color: var(--color-text-dark);
        font-size: 1.25rem;
        margin-right: 5px;
    }

    .second {
        color: var(--color-secondary);
    }
</style>
