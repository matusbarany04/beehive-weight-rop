<script>
    import PanelItem from "./PanelItem.svelte";
    import PanelHeaderItem from "./PanelHeaderItem.svelte";
    import Logo from "../../Logo.svelte";
    import {onMount} from "svelte";
    import {tweened} from "svelte/motion";
    import {quartInOut} from "svelte/easing";
    import PanelRoot from "./PanelRoot.svelte";

    let screenSize = 0;
    $: collapsed = screenSize < 400;

    const getWidth = (collapsed) => {
        // in rem
        return collapsed ? 3 : 17; // hardocded in panel item  !!
    };

    const width = tweened(getWidth(collapsed), {
        duration: 300,
        easing: quartInOut,
    });


</script>

<svelte:window bind:innerWidth={screenSize}/>


<PanelRoot>

    <Logo {collapsed}/>

    {#if !collapsed}
        <PanelHeaderItem title="Hlavné"/>
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
    />
    {#if !collapsed}
        <PanelHeaderItem title="Ostatné"/>
    {/if}

    <PanelItem
            {collapsed}
            text="Pomoc"
            svg="icons/lightbulb.svg"
            link="/dashboard/help"/>

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

    <div class="absolute bottom-2 text-white w-full text-center">
        <form action="/logoutUser" method="POST">
            <button type="submit">Odhlásiť sa</button>
        </form>
    </div>
</PanelRoot>

