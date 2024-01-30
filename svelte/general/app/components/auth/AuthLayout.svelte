<script>
  import CircleButton from "../../../../components/Buttons/CircleButton.svelte";

  export let imagePath = "/img/bg/scenery.jpeg";
  export let videoPath = "/img/bg/scenery_animation.mp4";

  const urlParams = new URLSearchParams(window.location.search);
  const invalid = urlParams.get("invalid");
</script>

<div class="v-screen relative flex h-screen flex-row">
  <div class="absolute left-0 top-0 z-20 p-4">
    <CircleButton type="primary" link="/" image="icons/arrow-left.svg"
    ></CircleButton>
  </div>
  <!--  banner div-->
  <div class="shadow-2xl sm:flex-1 hidden sm:block">
    <div class="relative h-full w-full bg-cover bg-right bg-no-repeat">
      <video
        autoplay
        loop
        muted
        playsinline
        class="h-full w-full object-cover object-right"
      >
        <source src={videoPath} type="video/mp4" />
      </video>
      <!-- Provide an alternative image as a background for the div -->
      <div
        class="hidden h-full w-full bg-cover bg-right bg-no-repeat"
        style="background-image: url({imagePath})"
      ></div>
    </div>
  </div>

  <!--  login form -->

  <div
    class="flex h-screen w-full max-w-full flex-1 items-center justify-center rounded-l-md bg-primary-100 shadow-2xl sm:max-w-sm md:max-w-md xl:max-w-xl"
  >
    <div
      class="box-border h-max max-w-md flex-col rounded-3xl p-8 text-tertiary-100 sm:space-y-5"
    >
      <slot />
    </div>
  </div>
</div>
