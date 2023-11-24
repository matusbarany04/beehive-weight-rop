<script>
  import RouterLink from "../../components/RouterLink.svelte";
  import NavigationBar from "../app/components/NavigationBar.svelte";
  import Button from "../../components/Buttons/Button.svelte";
  import { getLanguageInstance } from "../../components/language/languageRepository";
  import Root from "../layout/Root.svelte";

  const LI = getLanguageInstance();
</script>

<Root>
  <!-- background -->
  <div
    slot="beforeNavBar"
    class="absolute left-0 top-0 h-full w-full bg-cover bg-no-repeat"
    style:background-image="url('img/pattern_home.png')"
  ></div>

  <section id="home" class="flex h-screen flex-col">
    <div class="z-10 flex w-full flex-1 flex-row">
      <div class="left flex flex-1 flex-col justify-center p-8">
        <h1 class="mb-4 text-4xl font-bold text-slate-50">
          {LI.get("home.title")}
        </h1>

        <h5 class="text-xl text-slate-50">
          {LI.get("home.description")}
        </h5>
        <div class="mt-4 flex flex-row gap-4">
          <Button
            text={LI.get("home.button-buy")}
            image="icons/arrow-right.svg"
            imagePosition="right"
          />

          <Button
            type="transparent-white"
            image="icons/arrow-right.svg"
            imagePosition="right"
            text={LI.get("home.button-more")}
          />
        </div>
      </div>
      <div class="right hidden flex-1 place-items-center md:grid">
        <!-- Image of a beehive-->
        <div
          class="h-full w-full -scale-x-100 bg-contain bg-center bg-no-repeat"
          style:background-image="url('img/beeman/2.png')"
        ></div>
      </div>
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
