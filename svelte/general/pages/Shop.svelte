<script>
  import Root from "../layout/Root.svelte";
  import NavigationBar from "../app/components/NavigationBar.svelte";
  import Button from "../../components/Buttons/Button.svelte";
</script>

<svelte:head>
  <title>Shop</title>
  <meta name="Shop" content="Shop" />
</svelte:head>

<Root className="pt-4">
  <div class="mt-16 flex flex-1 items-center justify-center">
    <div
      class="box-border flex min-h-[50rem] max-w-7xl flex-1 flex-col p-4 text-slate-100 sm:flex-row"
    >
      <div class="box-border flex-1">
        <!-- large image -->
        <div
          class="mb-4 aspect-4/3 w-full rounded-xl bg-cover"
          style="background-image: url('/img/bg/scenery.jpeg')"
        ></div>
        <!-- other-->
        <div class="grid w-full grid-cols-3 grid-rows-1 gap-2">
          <div
            class="aspect-4/3 w-full rounded-xl bg-cover"
            style="background-image: url('/img/bg/scenery.jpeg')"
          ></div>
          <div
            class="aspect-4/3 w-full rounded-xl bg-cover"
            style="background-image: url('/img/bg/scenery.jpeg')"
          ></div>
          <div
            class="aspect-4/3 w-full rounded-xl bg-cover"
            style="background-image: url('/img/bg/scenery.jpeg')"
          ></div>
        </div>
      </div>
      <div class="box-border flex-1 p-4 md:ml-16">
        <h1 class="mb-8 text-4xl font-bold">
          Multifunkčné monitorovacie zariadenie
        </h1>
        <h2 class="text-4xl font-bold">Ref: BBMAP 2</h2>
        <!-- price -->
        <div class="my-4 w-full">
          <p class="inline text-xl font-bold text-confirm-600">150 €</p>

          <div
            class="inline-block h-4 w-4 bg-white bg-contain"
            style="
                 mask-image: url('/icons/info-circle.svg'); 
                 mask-repeat: no-repeat;
                 mask-size: contain ;  
                 mask-position: center
                "
          ></div>
        </div>
        <p class="text-slate-300">
          Multifunkčné monitorovacie zariadenie pre včelie úle s Wi-Fi
          pripojením umožňuje včelárom sledovať svoje včelstvá v reálnom čase.
          Schopné pripojiť rôzne senzory, ako teplota alebo vlhkosť, zabezpečuje
          komplexné dáta o stave úľa. Jeho otvorená architektúra umožňuje ľahkú
          integráciu s ďalšími smart zariadeniami a platformami, čím včelárom
          poskytuje flexibilitu a možnosť prispôsobiť si monitorovanie podľa ich
          potrieb. Celkovo prispeje k efektívnemu a udržateľnému včelárstvu.
        </p>

        <p class="my-6">na sklade</p>

        <Button text="Objednať"></Button>
      </div>
    </div>
  </div>
</Root>
