<script>
    import Panel from "../../../components/dashboard/panel/Panel.svelte";
    import PanelRoot from "../../../components/dashboard/panel/PanelRoot.svelte";


    export let data;


    let loading = false; 

</script>

<main class="w-full min-h-screen flex flex-1 box-border ">
    <Panel></Panel>
    <div class="bg-tertiary-100  flex-1 min-h-screen overflow-y-scroll px-8">
        <header class="h-16 flex align-middle">
            {#if !loading}
                <!--                <h1 class="text-3xl my-auto font-bold">{$message}</h1>-->
            {:else}
                <h1 class="text-3xl my-auto font-bold">Načítavam...</h1>
            {/if}
        </header>
        <!-- class={loading ? "hidden" : " "} -->
        {#if !loading}
            <slot />
        {:else}
            <!-- loading -->
            <div class="flex-1 h-5/6 flex items-center justify-center">
                <div class="lds-grid">
                    <div></div>
                    <div></div>
                    <div></div>
                    <div></div>
                    <div></div>
                    <div></div>
                    <div></div>
                    <div></div>
                    <div></div>
                </div>
            </div>
        {/if}
    </div>
</main>

<!-- <footer>
		<h2>hey!</h2>
	</footer> -->
<!-- </div> -->
<style>
    .lds-grid {
        display: inline-block;
        position: relative;
        width: 80px;
        height: 80px;
    }
    .lds-grid div {
        position: absolute;
        width: 16px;
        height: 16px;
        border-radius: 50%;
        background: #000000;
        animation: lds-grid 1.2s linear infinite;
    }
    .lds-grid div:nth-child(1) {
        top: 8px;
        left: 8px;
        animation-delay: 0s;
    }
    .lds-grid div:nth-child(2) {
        top: 8px;
        left: 32px;
        animation-delay: -0.4s;
    }
    .lds-grid div:nth-child(3) {
        top: 8px;
        left: 56px;
        animation-delay: -0.8s;
    }
    .lds-grid div:nth-child(4) {
        top: 32px;
        left: 8px;
        animation-delay: -0.4s;
    }
    .lds-grid div:nth-child(5) {
        top: 32px;
        left: 32px;
        animation-delay: -0.8s;
    }
    .lds-grid div:nth-child(6) {
        top: 32px;
        left: 56px;
        animation-delay: -1.2s;
    }
    .lds-grid div:nth-child(7) {
        top: 56px;
        left: 8px;
        animation-delay: -0.8s;
    }
    .lds-grid div:nth-child(8) {
        top: 56px;
        left: 32px;
        animation-delay: -1.2s;
    }
    .lds-grid div:nth-child(9) {
        top: 56px;
        left: 56px;
        animation-delay: -1.6s;
    }
    @keyframes lds-grid {
        0%,
        100% {
            opacity: 1;
        }
        50% {
            opacity: 0.5;
        }
    }
</style>
