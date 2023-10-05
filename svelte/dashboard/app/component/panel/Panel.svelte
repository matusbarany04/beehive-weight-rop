<script>
  import PanelItem from "./PanelItem.svelte";
  import PanelHeaderItem from "./PanelHeaderItem.svelte";
  import Logo from "../../../../components/Logo.svelte";
  import { onMount } from "svelte";
  import { tweened } from "svelte/motion";
  import { quartInOut } from "svelte/easing";
  import PanelRoot from "./PanelRoot.svelte";
  import { TW_BREAKPOINTS } from "../../../../components/lib/utils/staticFuncs";
  import panelState from "./panelState";
  import PanelDefault from "./PanelDefault.svelte";
  import EditPanel from "./EditPanel.svelte";

  let screenSize = 0;
  $: collapsed = false;

  let mode = panelState.getMode();

  panelState.getModeRef().subscribe((value) => {
    mode = value;
  });
</script>

<PanelRoot>
  {#if mode === "default"}
    <PanelDefault></PanelDefault>
  {:else if mode === "dashboardEdit"}
    <EditPanel></EditPanel>
  {:else}
    <div>Unknown color</div>
  {/if}
</PanelRoot>
