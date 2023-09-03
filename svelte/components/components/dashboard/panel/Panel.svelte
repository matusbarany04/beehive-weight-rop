<script>
    import PanelItem from "./PanelItem.svelte";
    import PanelHeaderItem from "./PanelHeaderItem.svelte";
    import Logo from "../../Logo.svelte";
    import { tweened } from "svelte/motion";
    import { quartInOut } from "svelte/easing";
    import PanelRoot from "./PanelRoot.svelte";

    let screenSize = 0;
    $: collapsed = screenSize < 1200;

    const getWidth = (collapsed) => {
        // in rem
        return collapsed ? 3 : 17; // hardocded in panel item  !!
    };

    const width = tweened(getWidth(collapsed), {
        duration: 300,
        easing: quartInOut,
    });

    // onMount(() => {
    //     const mediaQuery = window.matchMedia("(max-width: 800px)");
    //     collapsed = mediaQuery.matches;
    //     width.set(getWidth(collapsed));

    //     mediaQuery.addEventListener("change", function (e) {
    //         collapsed = mediaQuery.matches;
    //         width.set(getWidth(collapsed));
    //     });
    // };
</script>

<svelte:window bind:innerWidth={screenSize} />
<!-- style:width={$width + "rem"} -->
<PanelRoot >
    <!-- {#if collapsed}
        <div class="z-20 absolute top-4 -right-3 w-6 h-6 bg-primary-100 outline outline-2 outline-tertiary-100 rounded-full" />


        <div
            class="z-20 absolute top-4 -right-3 w-6 h-6 image bg-secondary-500"
            style=" mask-image: url(/icon/caret-right-fill.svg); -webkit-mask-image: url(/icon/caret-right-fill.svg); "
        />
    {/if} -->

    <Logo {collapsed} />
    <!-- <h1 class="text-slate-100">{screenSize}</h1>
    <h1 class="text-slate-100">{collapsed}</h1> -->
    <!-- {#if !collapsed} -->
    {#if !collapsed}
        <PanelHeaderItem title="Hlavné" />
    {/if}
    <!-- {/if} -->
    <PanelItem
        {collapsed}
        text="Hlavný panel"
        link="/auth/dashboard"
        svg="icons/dashboard.svg"
    />
    <PanelItem
        {collapsed}
        text="Úle"
        svg="icons/beehive.svg"
        link="/auth/dashboard/beehives"
    />
    {#if !collapsed}
        <PanelHeaderItem title="Ostatné" />
    {/if}
    <!-- TODO zmenit ikonu -->
    <PanelItem {collapsed} text="Pomoc" svg="icons/lightbulb.svg" link="/help" />

    <PanelItem
        {collapsed}
        text="Nastavenia"
        svg="icons/settings.svg"
        link="/auth/dashboard/settings"
    />
    <PanelItem
        {collapsed}
        text="Upozornenia"
        svg="icons/bubble.svg"
        link="/auth/dashboard/notifications"
    />
    <PanelItem
        {collapsed}
        text="Kalendár"
        svg="icons/calendar.svg"
        link="/auth/dashboard/calendar"
    />

    <div class="absolute bottom-0">
        <PanelItem text="odhlásiť sa" link="/auth/logout" />
    </div>
</PanelRoot>

<style lang="scss">
    .image {
        mask-size: 100%;
        height: 1.5rem;

        aspect-ratio: 1/1;
        background-size: contain;
        background-repeat: no-repeat;
        -webkit-mask-size: 100%;
        mask-size: 100%;
        /* background: black; */
    }
</style>
