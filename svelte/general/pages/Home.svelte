<script>
  import RouterLink from "../../components/RouterLink.svelte";
  import NavigationBar from "../app/components/NavigationBar.svelte";
  import Button from "../../components/Buttons/Button.svelte";
</script>

<main class="min-h-screen w-screen overflow-y-scroll bg-primary-100">
  <section id="home" class="flex h-screen flex-col">
    <NavigationBar></NavigationBar>
    <div class="flex w-full flex-1 flex-row">
      <div class="left flex flex-1 flex-col justify-center p-8">
        <h1 class="text-4xl font-bold text-slate-100">
          Moderné úľové váhy pre monitorovanie
        </h1>

        <h5 class="text-xl text-slate-100">
          BuzzyBees váhový systém včelieho úľa je monitorovací systém, ktorý
          okamžite informuje včelára o akýchkoľvek väčších zmenách v úli - váhe,
          teplote, vlhkosti a zvuku včiel.
        </h5>
        <div class="mt-4 flex flex-row gap-4">
          <Button
            text="Kúpiť"
            image="icons/arrow-right.svg"
            imagePosition="right"
          ></Button>

          <Button
            type="transparent"
            image="icons/arrow-right.svg"
            imagePosition="right"
            text="Zistiť viac"
          ></Button>
        </div>
      </div>
      <div class="right hidden flex-1 md:block">
        <!-- Image of a beehive-->
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
          Spokojných včelárov
        </p>
      </div>
      <div
        class="m-4 flex aspect-square flex-col items-center justify-center rounded-2xl bg-confirm-800"
      >
        <h1 class="text-center text-7xl font-bold text-secondary-500">25%</h1>
        <p class="text-center text-4xl text-slate-100 sm:text-2xl">
          Zvýšená produktivita
        </p>
      </div>
      <div
        class="m-4 flex aspect-square flex-col items-center justify-center rounded-2xl rounded-tl-[50%] bg-confirm-800"
      >
        <h1 class="text-center text-7xl font-bold text-secondary-500">99%</h1>
        <p class="text-center text-4xl text-slate-100 sm:text-2xl">
          Garantovaná Spoľhalivosť
        </p>
      </div>
    </div>
  </section>

  <section
    id="join"
    class="flex min-h-min w-full flex-col items-center justify-center px-8"
  >
    <h1 class="mb-4 text-center text-7xl font-bold text-slate-100">
      Pridajte sa k úľu!
    </h1>
    <p class="mb-4 max-w-3xl text-center text-2xl text-slate-100">
      Posuň svoje včelárstvo na vyššiu úroveň s najnovšími technológiami na
      riadenie úľov. Nepremeškaj príležitosť na sladšie úrody. Zaži ten
      vzrušujúci bzukot!
    </p>

    <Button type="primary" text="Kúpiť váhu"></Button>
  </section>
  <section id="spacer" class="h-64"></section>
  <section id="qna" class="min-h-screen w-full">
    <h1 class="mb-4 text-center text-4xl font-bold text-slate-100 md:mb-32">
      Bzučiace otázky
    </h1>
    <div class="mx-auto grid max-w-5xl grid-cols-1 gap-8 md:grid-cols-3">
      <div class="">
        <h4
          class="mx-auto w-5/6 text-center text-xl font-semibold text-slate-100 md:h-20 md:w-full md:text-left"
        >
          Ako jednoduchá je inštalácia?
        </h4>
        <p
          class="mx-auto w-5/6 text-center text-base text-slate-100 md:w-full md:text-left"
        >
          Je to hračka! Náš postupný sprievodca zabezpečuje bezproblémovú
          integráciu do vášho existujúceho včelieho úľu.
        </p>
      </div>
      <div class="">
        <h4
          class="mx-auto w-5/6 text-center text-xl font-semibold text-slate-100 md:h-20 md:w-full"
        >
          Je odolná voči poveternostným podmienkam?
        </h4>
        <p
          class="mx-auto w-5/6 text-center text-base text-slate-100 md:w-full md:text-center"
        >
          Absolútne áno! Naša váha je navrhnutá tak, aby vydržala vplyv počasia,
          vrátane extrémneho tepla, chladu a vlhkosti.
        </p>
      </div>
      <div class="">
        <h4
          class="mx-auto w-5/6 text-center text-xl font-semibold text-slate-100 md:h-20 md:w-full md:text-right"
        >
          Môžem pristupovať k údajom na diaľku?
        </h4>
        <p
          class="mx-auto w-5/6 text-center text-base text-slate-100 md:w-full md:text-right"
        >
          Áno! Naša jednoduchá aplikácia sa synchronizuje s vašou váhou,
          umožňujúc vám sledovať váš úľ z akéhokoľvek miesta.
        </p>
      </div>
    </div>
  </section>
</main>
