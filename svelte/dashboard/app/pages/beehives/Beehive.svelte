<script>
  /**
   * @fileoverview This page shows one beehive with its corresponding data
   * @module Beehive
   */
  import PercentageCard from "../../component/cards/PercentageCard.svelte";
  import Button from "../../../../components/Buttons/Button.svelte";
  import MapCard from "../../component/cards/MapCard.svelte";
  import ApexChart from "../../component/cards/ApexChart.svelte";
  import Modal from "../../../../components/Modal.svelte";
  import Input from "../../../../components/Inputs/Input.svelte";
  import DropdownInput from "../../../../components/Inputs/DropdownInput.svelte";
  import WeatherCard from "../../component/cards/WeatherCard.svelte";
  import shared, { onLoad } from "../../stores/shared";
  import RouterLink from "../../../../components/RouterLink.svelte";
  import message from "../../stores/message";
  import { TW_BREAKPOINTS } from "../../../../components/lib/utils/staticFuncs";
  import EChart from "../../component/cards/EChart.svelte";

  export let props;

  let user = shared.getUser();
  let showSettings = false;
  let beehive;
  let innerWidth;

  onLoad(["statuses", "beehives"] , (_b) => {
    beehive = shared.getBeehiveById(props.id);
    document.title = beehive.name;

  });

  message.setMessage("Detail úľu");
</script>

<svelte:window bind:innerWidth />
<div id="chart"></div>

<!-- {JSON.stringify(beeData)} -->
<!-- <div class="pt-2 p-4" /> -->

<div class="box-border h-full w-full">
  <div
    class="mx-auto mb-4 flex flex-col justify-between rounded-lg bg-white p-4 md:h-16 md:flex-row lg:w-5/6"
  >
    <h1 class=" text-2xl font-semibold">
      Váha {beehive?.name ? beehive?.name : "Loading..."}
    </h1>
    <div class="mt-4 md:mt-0">
      <RouterLink url="/edit" append>
        <Button text="Upraviť" />
      </RouterLink>
    </div>
  </div>
  <!--  <div class="mx-auto mb-4 grid grid-cols-1 justify-between gap-4 lg:w-5/6">-->
  <!--    <EChart></EChart>-->
  <!--  </div>-->
  {#if beehive}
    <div
      class="mx-auto mb-4 grid grid-cols-2 justify-between gap-4 md:grid-cols-4 lg:w-5/6"
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
              beehive_id: props.id,
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
              beehive_id: props.id,
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
              beehive_id: props.id,
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
              beehive_id: props.id,
            },
          ],
        }}
      />
    </div>

    <div
      class="mx-auto grid grid-cols-1 gap-4 md:aspect-video md:grid-cols-4 md:grid-rows-2 lg:w-5/6"
    >
      <EChart
        className="col-span-2 row-span-1"
        cardStates={{
          id: "",
          x: TW_BREAKPOINTS.md < innerWidth ? 1 : 0,
          y: TW_BREAKPOINTS.md < innerWidth ? 1 : 1,
          spanX: 2,
          mode: "static",
          spanY: 1,
          editing: false,
          title: "Vnútorná teplota váhy",
          data: [
            {
              type: "temperature",
              timespan: "week",
              beehive_id: props.id,
            },
          ],
        }}
      />

      <EChart
        className="col-span-2 row-span-1"
        cardStates={{
          id: "",
          x: TW_BREAKPOINTS.md < innerWidth ? 3 : 0,
          y: TW_BREAKPOINTS.md < innerWidth ? 1 : 2,
          spanX: 2,
          mode: "static",
          spanY: 1,
          editing: false,
          title: "Vnútorná vlhkosť váhy",
          data: [
            {
              type: "humidity",
              timespan: "week",
              beehive_id: props.id,
            },
          ],
        }}
      />

      <EChart
        className="col-span-2 row-span-1"
        cardStates={{
          id: "",
          x: TW_BREAKPOINTS.md < innerWidth ? 1 : 0,
          y: TW_BREAKPOINTS.md < innerWidth ? 2 : 3,
          spanX: 2,
          mode: "static",
          spanY: 1,
          editing: false,
          title: "Váha váhy",
          data: [
            {
              type: "weight",
              timespan: "week",
              beehive_id: props.id,
            },
          ],
        }}
      />

      <EChart
        className="col-span-2 row-span-1"
        cardStates={{
          id: "",
          spanX: 2,
          x: TW_BREAKPOINTS.md < innerWidth ? 3 : 0,
          y: TW_BREAKPOINTS.md < innerWidth ? 2 : 4,
          mode: "static",
          spanY: 1,
          editing: false,
          title: "Stav Batérie",
          data: [
            {
              type: "battery",
              timespan: "week",
              beehive_id: props.id,
            },
          ],
        }}
      />
    </div>

    <div class="mx-auto mt-4 grid grid-cols-1 gap-4 md:grid-cols-2 lg:w-5/6">
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
  {/if}

  <div class="spacer h-16">
    <!-- just for spacing-->
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
    class="my-4 flex flex-col gap-4"
  >
    <Input
      label="Názov váhy"
      placeholder="Názov"
      name="name"
      value={beehive?.name}
    />
    <input type="text" name="beehive_id" class="hidden" value={props.id} />

    <Input
      label="Poloha váhy"
      placeholder="Mesto"
      name="city"
      value={beehive?.location}
    />

    <DropdownInput
      label="Interval merania"
      name="interval"
      value={beehive?.interval + ""}
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
    />
  </button>
</Modal>
