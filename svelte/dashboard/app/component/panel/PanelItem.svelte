<script>
  import RouterLink from "../../../../components/RouterLink.svelte";
  import { route } from "../../../../components/router/route.serv";

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

  route.subscribe((val) => {
    if (selected != null) {
      isLinkActive = val === link;
    }
  });
</script>

<RouterLink baseRoute="true" url={link} reload={foreignLink}>
  <div class=" relative flex h-12 w-full cursor-pointer items-center">
    {#if isLinkActive}
      <div class="absolute -left-2 h-4/6 w-4 rounded-full bg-secondary-500" />
    {/if}
    <div
      class="border-box m-4 flex flex-1 items-center justify-items-center gap-4 rounded-xl p-1.5 hover:bg-primary-900"
    >
      <div
        class="relative ml-2 h-4 w-4 transition-all duration-300 ease-in isolation-isolate"
      >
        <div
          class={(isLinkActive ? "bg-secondary-500" : "bg-tertiary-100") +
            " overlay"}
          style="mask-image: url(/{svg}); -webkit-mask-image: url(/{svg}); "
        />
      </div>

      <p
        class={"visible font-normal opacity-100 transition-opacity delay-200 group-hover:text-secondary-500 " +
          (isLinkActive ? " text-secondary-500" : "text-tertiary-100")}
      >
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
