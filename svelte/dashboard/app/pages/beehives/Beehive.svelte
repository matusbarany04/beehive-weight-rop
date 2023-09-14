<script>
  import { page } from "$app/stores";
  // import { message } from "$lib/utils/dashboard";
  import { onMount } from "svelte";
  import PercentageCard from "../../../../components/dashboard/cards/PercentageCard.svelte";
  import Button from "../../../../components/Buttons/Button.svelte";
  import MapCard from "../../../../components/dashboard/cards/MapCard.svelte";
  import ApexChart from "../../../../components/dashboard/cards/ApexChart.svelte";
  import { dataHandler } from "../../../../components/dashboard/cards/dataHandler";
  import Modal from "../../../../components/Modal.svelte";
  import Input from "../../../../components/Inputs/Input.svelte";
  import DropdownInput from "../../../../components/Inputs/DropdownInput.svelte";
  import WeatherCard from "../../../../components/dashboard/cards/WeatherCard.svelte";

  export let data;
  $: ({ user, sessionid } = $page.data);
  let showSettings = false;

  onMount(async () => {
    message.set("Detail váhy");
  });

  // let temperatures = [];

  let beeData = dataHandler.getBeehiveData(data.beehive_id);
  let beeName = beeData.name;
  // let temperatures = dataHandler.getTemperatures(data.beehive_id)
</script>

<div id="chart" />

<!-- {JSON.stringify(beeData)} -->
<!-- <div class="pt-2 p-4" /> -->

<div class="w-full h-full box-border">
  <div
    class="mb-4 p-4 md:h-16 flex-col md:flex-row mx-auto flex justify-between bg-white rounded-lg lg:w-5/6"
  >
    <h1 class=" text-2xl font-semibold">Váha {beeName}</h1>
    <div class="mt-4 md:mt-0">
      <Button
        text="Upraviť"
        onClick={() => {
          showSettings = true;
        }}
      />
    </div>
  </div>
  <div
    class="mb-4 mx-auto justify-between lg:w-5/6 grid gap-4 grid-cols-2 md:grid-cols-4"
  >
    <PercentageCard
      cardStates={{
        id: "",
        mode: "static",
        title: "Vnútorná teplota váhy",
        spanX: 1,
        spanY: 1,
        editing: false,
        data: [
          {
            type: "temperature",
            from: "week",
            to: "now",
            comparison: {
              //TODO not implemented yet
              shown: false,
            },
            beehive_id: data.beehive_id,
          },
        ],
      }}
    />
    <PercentageCard
      cardStates={{
        id: "",
        mode: "static",
        title: "Váha váhy",
        spanX: 1,
        spanY: 1,
        editing: false,
        data: [
          {
            type: "weight",
            from: "week",
            to: "now",
            beehive_id: data.beehive_id,
          },
        ],
      }}
    />
    <PercentageCard
      cardStates={{
        title: "Vnútorná vlhkosť váhy",
        id: "",
        mode: "static",
        spanX: 1,
        spanY: 1,
        editing: false,
        data: [
          {
            type: "humidity",
            from: "week",
            to: "now",
            beehive_id: data.beehive_id,
          },
        ],
      }}
    />
    <PercentageCard
      cardStates={{
        id: "",
        mode: "static",
        title: "Stav batérie",
        spanX: 1,
        spanY: 1,
        editing: false,
        data: [
          {
            type: "battery",
            from: "week",
            to: "now",
            beehive_id: data.beehive_id,
          },
        ],
      }}
    />
  </div>

  <div class="grid gap-4 grid-cols-1 md:grid-cols-2 lg:w-5/6 mx-auto">
    <ApexChart
      cardStates={{
        id: "",
        spanX: 1,
        mode: "static",
        spanY: 1,
        editing: false,
        title: "Vnútorná teplota váhy",
        data: [
          {
            type: "temperature",
            timespan: "week",
            beehive_id: data.beehive_id,
          },
        ],
      }}
    />

    <ApexChart
      cardStates={{
        id: "",
        spanX: 1,
        mode: "static",
        spanY: 1,
        editing: false,
        title: "Vnútorná vlhkosť váhy",
        data: [
          {
            type: "humidity",
            timespan: "week",
            beehive_id: data.beehive_id,
          },
        ],
      }}
    />
    <ApexChart
      cardStates={{
        id: "",
        spanX: 1,
        mode: "static",
        spanY: 1,
        editing: false,
        title: "Váha váhy",
        data: [
          {
            type: "weight",
            timespan: "week",
            beehive_id: data.beehive_id,
          },
        ],
      }}
    />
    <ApexChart
      cardStates={{
        id: "",
        spanX: 1,
        mode: "static",
        spanY: 1,
        editing: false,
        title: "Stav Batérie",
        data: [
          {
            type: "battery",
            timespan: "week",
            beehive_id: data.beehive_id,
          },
        ],
      }}
    />

    <MapCard
      cardStates={{
        id: "",
        spanX: 1,
        spanY: 1,
        editing: false,
        title: "Mapa",
        mode: "static",
      }}
    />

    <WeatherCard
      cardStates={{
        id: "",
        spanX: 1,
        spanY: 1,
        editing: false,
        title: "Počasie",
        mode: "static",
      }}
    />
  </div>
</div>

<Modal bind:showModal={showSettings}>
  <h2 slot="header" class="text-2xl font-bold">
    {"Upraviť nastavenia váhy"}
  </h2>

  <form
    id="changeBeehiveSettings"
    action="?/saveSettings"
    method="POST"
    class="flex flex-col gap-4 my-4"
  >
    <Input label="Názov váhy" placeholder="Názov" name="name" value={beeName} />
    <input
      type="text"
      name="beehive_id"
      class="hidden"
      value={data.beehive_id}
    />

    <Input
      label="Poloha váhy"
      placeholder="Mesto"
      name="city"
      value={beeData.beehive.location}
    />

    <DropdownInput
      label="Interval merania"
      name="interval"
      value={beeData.beehive.interval + ""}
      small={"Upozornenie: Časté merania môžu výrazne skrátiť výdrž batérie."}
      options={[
        ["10", "10 minút"], //TODO opravit hodnoty
        ["60", "1 hodinu"],
        ["240", "4 hodiny"],
        ["480", "8 hodín"],
        ["1440", "1 deň"],
      ]}
    />
  </form>

  <button slot="footer" type="submit" form="changeBeehiveSettings">
    <Button
      slot="footer"
      type="confirm"
      autofocus
      onClick={() => {
        // saveSettings();
        // dialog.close();
      }}
      clickType="submit"
      text="Uložiť a zatvoriť okno"
    ></Button>
  </button>
</Modal>
