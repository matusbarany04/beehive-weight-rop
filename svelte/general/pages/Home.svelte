<script>
  import RouterLink from "../../components/RouterLink.svelte";
  import NavigationBar from "../app/components/NavigationBar.svelte";
  import Button from "../../components/Buttons/Button.svelte";
  import { getLanguageInstance } from "../../components/language/languageRepository";
  import Root from "../layout/Root.svelte";
  import Edge from "../../components/Edge.svelte";

  const LI = getLanguageInstance();

  let gridItems = [
    {
      title: "Hive Alerts!",
      icon: "chat-dots.svg",
      description:
        "Instant alerts for beehive weight changes, theft prevention, and no missed nectar flows.",
    },
    {
      title: "Remote Control",
      icon: "plus-solid.svg",
      description:
        "Effortless beehive management with our user-friendly remote control feature.",
    },
    {
      title: "Various Measurements",
      icon: "weight-hanging-solid-white.svg",
      description:
        "Track beehive weight, temperature, humidity, and sound for comprehensive monitoring.",
    },
    {
      title: "Plug and Play",
      icon: "plug-fill.svg",
      description:
        "Easy installation with step-by-step guides, ensuring seamless operation out of the box.",
    },
    {
      title: "Long-Lasting Battery",
      icon: "battery.svg",
      description:
        "Extended battery life—only one charge per beekeeping season.",
    },
    {
      title: "Cloud-Based Data",
      icon: "cloud-fill.svg",
      description:
        "Convenient cloud storage for data safety and accessibility.",
    },
  ];
</script>

<Root>
  <!-- background -->
  <!--  <div-->
  <!--    slot="beforeNavBar"-->
  <!--    class="absolute left-0 top-0 z-0 h-full w-full bg-cover bg-no-repeat"-->
  <!--    style:background-image="url('img/pattern_home.png')"-->
  <!--  ></div>-->

  <section
    id="home"
    class="relative z-10 m-auto mb-10 flex w-full max-w-7xl flex-col rounded-3xl sm:w-11/12"
    style="min-height: max(40rem, 90vh); height: 90vh"
  >
    <!-- title container -->
    <div
      class="absolute left-0 top-0 w-full max-w-[50rem] rounded-br-3xl sm:p-4 md:min-w-[30rem] md:bg-primary-500"
    >
      <h1
        class="mb-4 mt-4 w-full text-center text-6xl font-bold text-slate-50 sm:w-fit sm:text-6xl md:line-clamp-2 md:text-left"
      >
        {LI.get("home.title")}
      </h1>

      <Edge
        className="hidden md:block absolute top-0 -right-[24px]"
        rotation={180}
        colorClass="text-primary-500"
        size_px="24"
      ></Edge>

      <Edge
        className="hidden md:block absolute left-0 -bottom-[24px]"
        rotation={180}
        colorClass="text-primary-500"
        size_px="24"
      ></Edge>
    </div>

    <!-- image -->
    <div
      class=" absolute bottom-0 left-0 right-0 top-0 -z-10 flex h-full w-full flex-1 justify-items-stretch self-stretch justify-self-stretch rounded-3xl bg-secondary-300 bg-cover bg-right-bottom bg-no-repeat md:relative"
      style:background-image="url('img/bg/scenery.jpeg')"
    >
      <div
        class="flex-1 rounded-3xl bg-primary-100/10 backdrop-brightness-75 md:invisible"
      ></div>
    </div>

    <!-- subtitle container -->
    <div
      class="absolute bottom-0 right-0 mt-8 block flex-col-reverse items-center justify-items-center rounded-tl-3xl md:mt-0 md:flex md:w-3/5 md:min-w-[40rem] md:flex-row md:bg-primary-500 md:p-8"
    >
      <Button
        type="secondary"
        image="icons/arrow-right.svg"
        className="block h-min w-min md:mr-8 shrink-0 grow-0 mx-auto"
        imagePosition="right"
        text={LI.get("home.button-more")}
      />
      <h1
        class="mb-4 mt-8 flex-1 text-center text-base font-normal text-slate-50 md:mt-0 md:text-left md:text-slate-400"
      >
        {LI.get("home.description")}
      </h1>

      <Edge
        className="hidden md:block absolute right-0 -top-[24px]"
        colorClass="text-primary-500"
        rotation={0}
        size_px="24"
      ></Edge>

      <Edge
        className="hidden md:block absolute bottom-0 -left-[24px]"
        rotation={0}
        colorClass="text-primary-500"
        size_px="24"
      ></Edge>
    </div>
  </section>

  <section
    class="box-border block w-full flex-col items-center justify-center px-6"
  >
    <h1 class="mb-6 mt-16 text-center text-6xl font-bold text-slate-100">
      {"What can it do?"}
    </h1>
    <div
      class="md:4/5 mx-auto grid w-11/12 max-w-6xl grid-cols-2 gap-x-16 gap-y-8 text-white md:grid-cols-3"
    >
      {#each gridItems as { title, description, icon } (title)}
        <div class="flex flex-col items-center justify-center p-4 text-left">
          <div
            class="image mb-2 h-20 w-20 bg-secondary-600 bg-contain object-contain"
            style="mask-image: url('icons/{icon}'); -webkit-mask-image: url('icons/{icon}'); "
          ></div>
          <h3 class="my-2 text-center text-xl font-bold">{title}</h3>
          <p class="text-gray-600 hidden text-center text-sm sm:inline-block">
            {description}
          </p>
        </div>
      {/each}
    </div>
  </section>

  <section
    id="statistics"
    class="flex min-h-screen w-full flex-row items-center justify-center"
  >
    <div
      class="grid w-full max-w-7xl grid-cols-1 gap-1 px-10 sm:grid-cols-3 sm:gap-2 md:gap-16"
    >
      <div
        class="m-4 flex aspect-square flex-col items-center justify-center rounded-bl-[50%] rounded-br-[50%] rounded-tl-2xl rounded-tr-[50%] bg-confirm-800"
      >
        <h1 class="text-center text-7xl font-bold text-secondary-500">100%</h1>
        <p class="text-center text-4xl text-slate-100 sm:text-2xl">
          {LI.get("pros.first")}
        </p>
      </div>
      <div
        class="m-4 flex aspect-square flex-col items-center justify-center rounded-2xl bg-confirm-800"
      >
        <h1 class="text-center text-7xl font-bold text-secondary-500">25%</h1>
        <p class="text-center text-4xl text-slate-100 sm:text-2xl">
          {LI.get("pros.second")}
        </p>
      </div>
      <div
        class="m-4 flex aspect-square flex-col items-center justify-center rounded-2xl rounded-tl-[50%] bg-confirm-800"
      >
        <h1 class="text-center text-7xl font-bold text-secondary-500">99%</h1>
        <p class="text-center text-4xl text-slate-100 sm:text-2xl">
          {LI.get("pros.third")}
        </p>
      </div>
    </div>
  </section>

  <section
    id="join"
    class="flex min-h-min w-full flex-col items-center justify-center px-8"
  >
    <h1 class="mb-4 text-center text-7xl font-bold text-slate-100">
      {LI.get("join.title")}
    </h1>
    <p class="mb-4 max-w-3xl text-center text-2xl text-slate-100">
      {LI.get("join.description")}
    </p>

    <Button type="primary" text={LI.get("join.button-buy")} />
  </section>
  <section id="spacer" class="h-64"></section>
  <section id="qna" class="min-h-screen w-full">
    <h1 class="mb-4 text-center text-4xl font-bold text-slate-100 md:mb-32">
      {LI.get("faq.title")}
    </h1>
    <div class="mx-auto grid max-w-5xl grid-cols-1 gap-8 md:grid-cols-3">
      <div class="">
        <h4
          class="mx-auto w-5/6 text-center text-xl font-semibold text-slate-100 md:h-20 md:w-full md:text-left"
        >
          {LI.get("faq.first.question")}
        </h4>
        <p
          class="mx-auto w-5/6 text-center text-base text-slate-100 md:w-full md:text-left"
        >
          {LI.get("faq.first.answer")}
        </p>
      </div>
      <div class="">
        <h4
          class="mx-auto w-5/6 text-center text-xl font-semibold text-slate-100 md:h-20 md:w-full"
        >
          {LI.get("faq.second.question")}
        </h4>
        <p
          class="mx-auto w-5/6 text-center text-base text-slate-100 md:w-full md:text-center"
        >
          {LI.get("faq.second.answer")}
        </p>
      </div>
      <div class="">
        <h4
          class="mx-auto w-5/6 text-center text-xl font-semibold text-slate-100 md:h-20 md:w-full md:text-right"
        >
          {LI.get("faq.third.question")}
        </h4>
        <p
          class="mx-auto w-5/6 text-center text-base text-slate-100 md:w-full md:text-right"
        >
          {LI.get("faq.third.answer")}
        </p>
      </div>
    </div>
  </section>
</Root>

<style>
  .image {
    mask-size: 100%;
    mask-repeat: no-repeat;
    mask-position: center;
    aspect-ratio: 1/1;
    background-size: contain;
    background-repeat: no-repeat;
    -webkit-mask-size: 100%;
  }
</style>
