<script>
  import RouterLink from "../../components/RouterLink.svelte";
  import NavigationBar from "../app/components/NavigationBar.svelte";
  import Button from "../../components/Buttons/Button.svelte";
</script>

<main class="w-screen min-h-screen bg-primary-100 overflow-y-scroll">
  <section id="home" class="h-screen flex flex-col">
    <NavigationBar></NavigationBar>
    <div class="w-full flex-1 flex flex-row">
      <div class="left flex-1 p-8 flex justify-center flex-col">
        <h1 class="text-slate-100 text-4xl font-bold">
          Moderné úľové váhy pre monitorovanie
        </h1>
        <h5 class="text-slate-100 text-xl">
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
      <div class="hidden md:block right flex-1"></div>
    </div>
  </section>
  <section
    id="statistics"
    class="w-full min-h-screen flex flex-row items-center justify-center"
  >
    <div
      class="w-full grid grid-cols-1 sm:grid-cols-3 px-10 gap-1 sm:gap-2 md:gap-16"
    >
      <div
        class="m-4 aspect-square rounded-tl-2xl rounded-tr-[50%] rounded-br-[50%] rounded-bl-[50%] bg-confirm-800 flex flex-col justify-center items-center"
      >
        <h1 class="text-secondary-500 text-7xl font-bold text-center">100%</h1>
        <p class="text-4xl text-slate-100 text-center sm:text-2xl">
          Spokojných včelárov
        </p>
      </div>
      <div
        class="aspect-square rounded-2xl bg-confirm-800 flex flex-col justify-center items-center m-4"
      >
        <h1 class="text-secondary-500 text-7xl font-bold text-center">25%</h1>
        <p class="text-4xl text-slate-100 text-center sm:text-2xl">
          Zvýšená produktivita
        </p>
      </div>
      <div
        class="m-4 aspect-square rounded-tl-[50%] rounded-2xl bg-confirm-800 flex flex-col justify-center items-center"
      >
        <h1 class="text-secondary-500 text-7xl font-bold text-center">99%</h1>
        <p class="text-4xl text-slate-100 text-center sm:text-2xl">
          Garantovaná Spoľhalivosť
        </p>
      </div>
    </div>
  </section>

  <section
    id="join"
    class="w-full min-h-min flex flex-col items-center justify-center px-8"
  >
    <h1 class="text-7xl font-bold text-slate-100 text-center">
      Pridajte sa k úľu!
    </h1>
    <p class="mb-4 max-w-3xl text-slate-100 text-2xl text-center">
      Posuň svoje včelárstvo na vyššiu úroveň s najnovšími technológiami na
      riadenie úľov. Nepremeškaj príležitosť na sladšie úrody. Zaži ten
      vzrušujúci bzukot!
    </p>

    <Button type="primary" text="Kúpiť váhu"></Button>
  </section>
  <section id="spacer" class="h-64"></section>
</main>
