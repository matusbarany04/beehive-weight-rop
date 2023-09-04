<script>
    import RouterLink from "../../../RouterLink.svelte";
    import {route} from "../../../route.serv";
    
    /**
     * Display text for the panel item.
     *
     * @type {String}
     */
    export let text;

    /**
     * Path for the icon of the panel item.
     *
     * @type {String}
     */
    export let svg;

    /**
     * URL for the navigation.
     *
     * @type {String|null}
     * @default null
     */
    export let link = null;

    /**
     * Indicates if the link is external to the SPA.
     *
     * @type {Boolean}
     * @default false
     */
    export let foreignLink = false;

    /**
     * Determines if the current panel item is selected.
     *
     * @type {Boolean}
     */
    export let selected = false;

    /**
     * Indicates if the sidebar or panel is in a collapsed state.
     *
     * @type {Boolean}
     */
    export let collapsed = false;
    
    
    let isLinkActive = false;

  
    route.subscribe(val => {
        if (selected != null) {
            isLinkActive = val === link;
        }
    });
    
</script>

<RouterLink baseRoute="true" url={link} reload={foreignLink}>
    <div class=" w-full h-12 relative flex items-center cursor-pointer" >
        {#if isLinkActive}
            <div class="w-4 absolute -left-2 h-4/6 bg-secondary-500 rounded-full"/>
        {/if}
        <div class="hover:bg-primary-900 flex-1 flex gap-4 m-4 items-center justify-items-center p-1.5 border-box rounded-xl">

            <div class="transition-all duration-300 ease-in relative ml-2  w-4 h-4 isolation-isolate ">
                <div class={(isLinkActive ? "bg-secondary-500" : "bg-tertiary-100") +
                " overlay"} style="mask-image: url(/{svg}); -webkit-mask-image: url(/{svg}); "
                />
            </div>

            <p class={"font-normal delay-200 visible opacity-100 transition-opacity group-hover:text-secondary-500 "
           + (isLinkActive ? " text-secondary-500" : "text-tertiary-100")}>
                {text}
            </p>
        </div>
    </div>
</RouterLink>

<style lang="scss">

  .overlay {
    position: absolute;
    width: 16px;
    height: 16px;
    background-size: cover;
    -webkit-mask-size: 100%;
    mask-size: 100%;
  }

</style>
