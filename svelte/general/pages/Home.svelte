<script>
  import RouterLink from "../../components/RouterLink.svelte";
  import NavigationBar from "../app/components/NavigationBar.svelte";
  import Button from "../../components/Buttons/Button.svelte";
</script>

<main class="w-screen min-h-screen bg-primary-100">
  <section id="home" class="h-screen flex flex-col">
    <NavigationBar></NavigationBar>
    <div class="w-full flex-1 flex flex-row">
      <div class="left flex-1 p-8 flex justify-center flex-col">
        <h1 class="text-slate-100 text-4xl font-bold">
          Moderné úľové váhy pre monitorovanie
        </h1>
        <h5 class="text-slate-100 text-xl">
          BeeVerse váhový systém včelieho úľa je monitorovací systém, ktorý
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
</main>
