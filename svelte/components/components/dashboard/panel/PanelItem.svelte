<script>
    // import { page } from "$app/stores";

    export let text;
    export let svg;
    export let link = null;
    export let selected = false;
    export let collapsed = false;
    let inner_selected = false;
    
    function getPath(currentPath) {
        if (selected != null) {
            inner_selected = currentPath == link;
        }
    }
    
     $: getPath(window.location.pathname);
</script>
<!--  position: absolute;
        width: 15px;
        left: -10px;
        min-height: 80%;
        border-radius: 1000rem;
        background-color: var(--color-secondary); -->
<a class={"cont group"} href={link}>
    {#if inner_selected}
        <div class="w-4 absolute -left-2 h-5/6 bg-secondary-500 rounded-full"  />
    {/if}

    <div class={"imageIcon collapsed"}>
        <div
            class={(inner_selected ? "selected" : "unselected") +
                " overlay smh "}
            style="mask-image: url(/{svg}); -webkit-mask-image: url(/{svg}); "
        />
    </div>

    <!-- in:fly={{delay: 100, duration: 5000 ,x: -50}}
           out:fly={{duration: 5000 ,x: 50}} -->
    <!-- {#if !collapsed} -->
    <p class={"font-normal text-tertiary-100 media_hidden delay-200 visible opacity-100 transition-opacity group-hover:text-secondary-500 " + (inner_selected ? " selectedtext" : "")}>
        {text}
    </p>
    <!-- {/if} -->
</a>

<style lang="scss">
  .cont {
    height: 3rem;
    display: block;

    position: relative;
    display: flex;
    align-items: center;
    gap: 10px;
    cursor: pointer;
  }
  .media_hidden {
    @media (max-width: 1200px) {
      visibility: hidden;
      opacity: 0;
      transition-delay: 200ms;
      transition: opacity 0.3s, visibility 0.3s;
    }
  }
  .imageIcon {
    transition: all 300ms ease;
    margin-left: 50px;

    isolation: isolate;
    -webkit-isolation: isolate;
    position: relative;
    width: 20px;
    height: 20px;
  }

  .collapsed {
    /* // padding: 0px;
    // display: flex;

    // margin-left: 5px; */

    @media (max-width: 1200px) {
      margin-left: calc(calc(3rem - 20px) / 2);
      transition: all 300ms ease;
    }
  }

  .overlay {
    position: absolute;
    width: 20px;
    height: 20px;
    background-size: cover;
    /* // -webkit-mask-mode: alpha; */
    -webkit-mask-size: 100%;
    mask-size: 100%;
    /* // mask-mode: alpha; */
  }

  .selected {
    background: var(--color-secondary);
  }
  .unselected {
    background: var(--color-text-dark);
  }
  .selectedtext {
    color: var(--color-secondary);
  }

  .cont:hover > .text {
    color: var(--color-secondary);
  }

  .cont:hover .smh {
    background: var(--color-secondary) !important;
  }
</style>
