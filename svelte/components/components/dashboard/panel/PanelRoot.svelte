<script>
    import {tweened} from "svelte/motion";
    import {quartInOut} from "svelte/easing";

    // let screenSize;
    // let collapsed = screenSize < 1200;
    //
    // const getWidth = (collapsed) => {
    //     // in rem
    //     return collapsed ? 3 : 17; // hardocded in panel item  !!
    // };
    //
    // const width = tweened(getWidth(collapsed), {
    //     duration: 300,
    //     easing: quartInOut,
    // });

    // onMount(() => {
    //     const mediaQuery = window.matchMedia("(max-width: 1200px)");
    //     collapsed = mediaQuery.matches;
    //     width.set(getWidth(collapsed));

    //     mediaQuery.addEventListener("change", function (e) {
    //         collapsed = mediaQuery.matches;
    //         width.set(getWidth(collapsed));
    //     });
    // });
</script>
<!--<svelte:window bind:innerWidth="{screenSize}"/>-->
<!-- style:width={$width + "rem"} -->
<!--<div class="panel h-screen max-h-screen -->
<!--relative bg-primary-500 flex flex-col duration-300  ease-in-out transition-all w-96 sm:w-8">-->
<!--&lt;!&ndash;    <slot></slot>&ndash;&gt;-->
<!--</div>-->

<div class="divv w-96 h-96 bg-primary-500"></div>
<style>
    /*.panel {*/
    /*    width: 17rem;*/
    /*}*/
     .divv{
         width: 17rem;
         height: 17rem;
     }
</style>


