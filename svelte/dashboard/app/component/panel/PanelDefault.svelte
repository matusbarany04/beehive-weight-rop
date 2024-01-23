<script>
  import PanelItem from "./PanelItem.svelte";
  import PanelHeaderItem from "./PanelHeaderItem.svelte";
  import Logo from "../../../../components/Logo.svelte";
  import { onMount } from "svelte";
  import { tweened } from "svelte/motion";
  import { quartInOut } from "svelte/easing";
  import PanelRoot from "./PanelRoot.svelte";
  import sf from "../../../../components/lib/utils/staticFuncs";
  import { getLanguageInstance } from "../../../../components/language/languageRepository";

  let screenSize = 0;
  $: collapsed = false;

  const li = getLanguageInstance();
</script>

<svelte:window bind:innerWidth={screenSize} />

<Logo {collapsed} />

{#if !collapsed}
  <PanelHeaderItem title="Hlavné" />
{/if}

<PanelItem
  {collapsed}
  text={li.get("sidenav.home")}
  link="/dashboard"
  svg="icons/dashboard.svg"
/>
<PanelItem
  {collapsed}
  text={li.get("sidenav.beehives")}
  svg="icons/beehive.svg"
  link="/dashboard/beehives"
  hasSubpages={true}
/>
{#if !collapsed}
  <PanelHeaderItem title={li.get("sidenav.other")} />
{/if}

<PanelItem
  {collapsed}
  text={li.get("sidenav.help")}
  svg="icons/lightbulb.svg"
  link="/dashboard/help"
/>

<PanelItem
  {collapsed}
  text={li.get("sidenav.settings")}
  svg="icons/settings.svg"
  link="/dashboard/settings"
/>
<PanelItem
  {collapsed}
  text={li.get("sidenav.notifications")}
  svg="icons/bubble.svg"
  link="/dashboard/notifications"
/>
<PanelItem
  {collapsed}
  text={li.get("sidenav.calendar")}
  svg="icons/calendar.svg"
  link="/dashboard/calendar"
/>
<!--<PanelItem-->
<!--  {collapsed}-->
<!--  text="Test"-->
<!--  svg="icons/chat-dots.svg"-->
<!--  link="/dashboard/test"-->
<!--/>-->
<div class="absolute bottom-2 w-full text-center text-white">
  <form action="/logoutUser" method="POST">
    <button type="submit">{li.get("sidenav.logout")}</button>
  </form>
</div>
