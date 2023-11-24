<script>
  import Root from "../layout/Root.svelte";
  import NavigationBar from "../app/components/NavigationBar.svelte";
  import Button from "../../components/Buttons/Button.svelte";
</script>

<svelte:head>
  <title>Shop</title>
  <meta name="Shop" content="Shop" />
</svelte:head>

<Root>
  <NavigationBar />

  <div
    class="relative mx-auto mt-16 box-border flex max-w-7xl flex-col p-4 text-white md:flex-row"
  >
    <div class="box-border flex-1 px-16">
      <div
        class="grid aspect-square place-items-center rounded-xl outline outline-tertiary-100"
      >
        <img src="/img/sketches/3.png" />
      </div>
    </div>
    <div class="box-border h-full flex-1 p-16 pb-0 md:p-0">
      <h1 class="title mb-2 text-6xl">BBMAP 2</h1>
      <h5 class="mb-4 text-4xl">
        <b class="text-secondary-500">14.30€</b> (bez DPH
        <b class="text-secondary-500"> 11.92€</b> )
      </h5>
      <h1 class="description text-lg">
        Multifunkčné monitorovacie zariadenie pre včelie úle, komunikujúce cez
        wi-fi s možnosťou zapojenia rozličných senzorov a zariadení
      </h1>
      <Button className="my-4" text="Kúpiť" type="primary"></Button>

      <img src="/img/beeman/5.png" class="md:h-64" />
    </div>
  </div>
</Root>
