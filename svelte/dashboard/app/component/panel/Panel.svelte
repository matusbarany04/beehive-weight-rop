<script>
  import PanelItem from "./PanelItem.svelte";
  import PanelHeaderItem from "./PanelHeaderItem.svelte";
  import Logo from "../../../../components/Logo.svelte";
  import { onMount } from "svelte";
  import { tweened } from "svelte/motion";
  import { quartInOut } from "svelte/easing";
  import PanelRoot from "./PanelRoot.svelte";
  import { TW_BREAKPOINTS } from "../../../../components/lib/utils/staticFuncs";

  let screenSize = 0;
  $: collapsed = false;
</script>

<svelte:window bind:innerWidth={screenSize} />

<PanelRoot>
  <Logo {collapsed} />

  {#if !collapsed}
    <PanelHeaderItem title="Hlavné" />
  {/if}

  <PanelItem
    {collapsed}
    text="Hlavný panel"
    link="/dashboard"
    svg="icons/dashboard.svg"
  />
  <PanelItem
    {collapsed}
    text="Úle"
    svg="icons/beehive.svg"
    link="/dashboard/beehives"
    hasSubpages={true}
  />
  {#if !collapsed}
    <PanelHeaderItem title="Ostatné" />
  {/if}

  <PanelItem
    {collapsed}
    text="Pomoc"
    svg="icons/lightbulb.svg"
    link="/dashboard/help"
  />

  <PanelItem
    {collapsed}
    text="Nastavenia"
    svg="icons/settings.svg"
    link="/dashboard/settings"
  />
  <PanelItem
    {collapsed}
    text="Upozornenia"
    svg="icons/bubble.svg"
    link="/dashboard/notifications"
  />
  <PanelItem
    {collapsed}
    text="Kalendár"
    svg="icons/calendar.svg"
    link="/dashboard/calendar"
  />

  <div class="absolute bottom-2 w-full text-center text-white">
    <form action="/logoutUser" method="POST">
      <button type="submit">Odhlásiť sa</button>
    </form>
  </div>
</PanelRoot>
