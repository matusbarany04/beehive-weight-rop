<script>
  import Panel from "../component/panel/Panel.svelte";
  import PanelRoot from "../component/panel/PanelRoot.svelte";
  import message from "../stores/message";
  import panelState from "../component/panel/panelState";
  import CircleButton from "../../../components/Buttons/CircleButton.svelte";
  import {
    getRouteDepth,
    route,
    traverseBack,
  } from "../../../components/router/route.serv";

  let headerMessage = message.getMessage();

  let circleButton = false;

  route.subscribe((route) => {
    circleButton = getRouteDepth() > 2;
  });

  message.getMessageRef().subscribe((message) => {
    headerMessage = message;
  });

  let leftPadding = false;

  panelState.getOpenedRef().subscribe((val) => {
    leftPadding = !val;
  });

  let loading = false;
</script>

<main class="box-border flex min-h-screen w-full flex-1 select-none">
  <Panel />
  <div
    class="h-screen flex-1 overflow-y-scroll bg-tertiary-100 px-2 sm:px-4 md:px-8"
  >
    <header class="flex h-16 align-middle">
      {#if !loading}
        {#if circleButton}
          <div class="ml-8 p-4 lg:ml-0">
            <CircleButton
              type="primary"
              image="icons/arrow-left.svg"
              onClick={() => {
                traverseBack(1);
              }}
            ></CircleButton>
          </div>
        {/if}
        <h1 class=" my-auto text-3xl font-bold {leftPadding ? 'ml-8' : ''}">
          {headerMessage}
        </h1>
      {:else}
        <h1 class="my-auto text-3xl font-bold">Načítavam...</h1>
      {/if}
    </header>
    <!-- class={loading ? "hidden" : " "} -->
    {#if !loading}
      <slot />
    {:else}
      <!-- loading -->
      <div class="flex h-5/6 flex-1 items-center justify-center">
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
