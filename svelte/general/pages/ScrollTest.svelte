<script>
  console.log("what is happening ");
  import { onMount, tick } from "svelte";

  let scrollProgress = 0;

  function handleScroll() {
    const scrollPercentage =
      window.pageYOffset / (document.body.offsetHeight - window.innerHeight);
    console.log(scrollPercentage);
    // Set the animation progress based on the scroll percentage
    scrollProgress = scrollPercentage;
  }

  window.addEventListener("scroll", handleScroll);

  // Add a scroll event listener to handle the animation on scroll
  onMount(() => {
    console.log("mounted");

    // Cleanup the event listener on component unmount
    // return () => {
    //   window.removeEventListener('scroll', handleScroll);
    // };
  });
</script>

<div style="height: 50vh; background-color: #f0f0f0;"></div>

<div
  style="--scroll: {scrollProgress} "
  class="scroll-animated-element bg-red fixed mx-auto h-96 w-96"
></div>

<!-- Additional content to make the page scrollable -->
<div style="height: 200vh; background-color: #f0f0f0;"></div>

<style>
  /* Define the scroll-animated-element styles using Tailwind CSS classes */
  .scroll-animated-element {
    /*transform: translateY(var(--scroll-progress, 0));*/
    /*animation: colorChange 2s ease infinite;*/
    /*animation-timeline: color-change-timeline;*/
    /*animation-range: 0% 100%;*/

    animation: rotate 1s linear infinite;
    animation-play-state: paused;
    animation-delay: calc(var(--scroll) * -1s);
  }

  @keyframes rotate {
    to {
      transform: rotate(360deg);
    }
  }

  /* Keyframes for the color change animation */
  @keyframes colorChange {
    from {
      background-color: lightblue;
    }
    to {
      background-color: coral;
    }
  }

  /* Define a scroll timeline named "color-change-timeline" */
  @scroll-timeline color-change-timeline {
    scroll-timeline-axis: vertical;
  }
</style>
