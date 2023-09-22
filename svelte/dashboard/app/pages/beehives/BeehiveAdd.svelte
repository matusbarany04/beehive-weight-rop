<script>
  /**
   * @fileoverview This page allows beekeper to add new beehives to their account with multiple options
   * @module BeehiveAdd
   */

  import Option from "../../component/beehives/Option.svelte";
  import message from "../../stores/message";

  message.setMessage("Pridať úľ");
</script>

<svelte:head>
  <title>Úle</title>
  <meta name="Pridať úl" content="Úle" />
</svelte:head>
<div
  class="mx-auto mb-4 justify-between gap-4 rounded-lg lg:w-5/6 lg:flex-row lg:justify-center"
>
  <div
    class="mb-5 rounded-lg bg-white p-5 md:border-0 md:border-r-2 md:border-slate-300 md:pr-2"
  >
    <h1 class="text-4xl">Pridať nové zariadenie</h1>
  </div>

  <Option url="/token">Mám identifikačné číslo váhy</Option>

  <Option url="/usb">Použiť USB kábel</Option>

  <Option url="/bluetooth">Pripojiť váhu pomocou Bluetooth</Option>

  <Option url="/app">
    Stianuť aplikáciu pre pripojenie z mobilného telefónu
  </Option>

  <!--
  <div class="items-center md:justify-center">
    <form method="POST" class="flex flex-1 flex-col md:items-center">
      <div>
        <label for="name" class="">Názov</label> <br />
        <input
          type="text"
          name="name"
          class="mb-2 mt-1 h-8 w-full rounded-md border-2 border-slate-300 pl-4 sm:w-96"
        />
      </div>
      <br />
      <div>
        <label for="beehive_id">Identifikačné čislo váhy</label> <br />
        <input
          type="text"
          name="beehive_id"
          class="mb-2 mt-1 h-8 w-full rounded-md border-2 border-slate-300 pl-4 sm:w-96"
        />
      </div>
      <br />
      <div>
        <label for="interval">Interval merania</label> <br />
        <input
          type="text"
          name="interval"
          class="mb-2 mt-1 h-8 w-full rounded-md border-2 border-slate-300 pl-4 sm:w-96"
        />
        <br />
        <button type="submit" class="mt-4">
          <Button type="primary" text="Pridať"></Button>
        </button>
      </div>
    </form>
  </div>-->
</div>
