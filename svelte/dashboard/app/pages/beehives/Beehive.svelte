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
  import { onMount, tick } from "svelte";

  export let props;

  let user = shared.getUser();
  let showSettings = false;
  /** @type {BeehiveObj} */
  let beehive;
  let innerWidth;
  let container;
  let rowHeight = 0;
  let rowCount = 0;
  onLoad(["statuses", "beehives"], (_b) => {
    beehive = shared.getBeehiveById(props.id);
    console.log("Beehive loaded", beehive);
    document.title = beehive.name;
    tick().then(() => {
      resize();
    });
  });

  message.setMessage("Detail úľu");
  let small = undefined;

  let getRowCount = () => {
    return !small
      ? 1 + Math.ceil(beehive.getCurrentDataTypes(true).length / 2)
      : 2 + Math.ceil(beehive.getCurrentDataTypes(true).length);
  };

  const resize = (e) => {
    small = window.innerWidth <= TW_BREAKPOINTS.md;
    rowCount = getRowCount();
    updateRowHeight();
  };

  function updateRowHeight() {
    console.log("container", container);
    if (container) {
      rowHeight = container.clientWidth / 4;
    } else {
      console.error("error no container");
    }
  }

  let resizeObserver;

  onMount(() => {
    updateRowHeight(); // Initial update

    resizeObserver = new ResizeObserver(() => {
      updateRowHeight();
    });

    onLoad(["statuses", "beehives"], (_b) => {
      // TODO this is hellishly slow, figure this out using some css classes
      tick().then(() => {
        resizeObserver.observe(container);
      });
    });

    return () => {
      if (resizeObserver) {
        resizeObserver.disconnect();
      }
    };
  });
</script>

<div id="chart"></div>

<svelte:window on:resize={resize} />
<!-- {JSON.stringify(beeData)} -->
<!-- <div class="pt-2 p-4" /> -->

<div class="box-border h-full w-full">
  <div
    class="mx-auto mb-4 flex flex-col justify-between rounded-lg bg-white p-4 md:h-16 md:flex-row lg:w-5/6"
  >
    <h1 class=" text-2xl font-semibold">
      Váha {beehive?.name
        ? beehive?.name +
          " - " +
          beehive.devices.length +
          " " +
          rowCount +
          " " +
          rowHeight
        : "Loading..."}
    </h1>
    <div class="mt-4 md:mt-0">
      <RouterLink url="/edit" append>
        <Button text="Upraviť" />
      </RouterLink>
    </div>
  </div>

  {#if beehive}
    <div
      class="mx-auto mb-4 grid grid-cols-2 justify-between gap-4 sm:grid-cols-4 lg:w-5/6"
    >
      <PercentageCard
        cardStates={{
          mode: "static",
          title: "Váha váhy",
          data: [
            {
              type: "weight",
              beehive_id: props.id,
            },
          ],
        }}
      />
      <PercentageCard
        cardStates={{
          title: "Status zariadenia",
          mode: "static",
          data: [
            {
              type: "status",
              beehive_id: props.id,
            },
          ],
        }}
      />

      <PercentageCard
        cardStates={{
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

      <PercentageCard
        cardStates={{
          mode: "static",
          title: "Dáta z pripojeného sensora",
          spanX: 1,
          spanY: 1,
          editing: false,
          data: [
            {
              type: "temperature",
              from: "week",
              to: "now",
              beehive_id: props.id,
            },
          ],
        }}
      />
    </div>

    <div
      bind:this={container}
      class="mx-auto grid w-full grid-cols-2 gap-4 md:grid-cols-4 lg:w-5/6"
      style="grid-template-rows: repeat({rowCount}, {rowHeight +
        rowHeight * small}px) !important;"
    >
      {#if small !== undefined}
        <EChart
          className="col-span-2 row-span-1"
          cardStates={{
            id: "",
            mode: "static",
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

        {#each beehive.getCurrentDataTypes(true) as type}
          <EChart
            className="col-span-2 row-span-1"
            cardStates={{
              id: "",
              spanX: 2,
              mode: "static",
              spanY: 1,
              editing: false,
              title: type + " váhy",
              data: [
                {
                  type: type,
                  timespan: "week",
                  beehive_id: props.id,
                },
              ],
            }}
          />
        {/each}
      {/if}
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
