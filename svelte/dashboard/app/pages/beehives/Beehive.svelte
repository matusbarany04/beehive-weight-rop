<script>
  /**
   * @fileoverview This page shows one beehive with its corresponding data
   * @module Beehive
   */
  import PercentageCard from "../../component/cards/PercentageCard.svelte";
  import Button from "../../../../components/Buttons/Button.svelte";
  import MapCard from "../../component/cards/MapCard.svelte";
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

<svelte:head>
  <title>Úľ</title>
  <meta name="Úľ" content="Úľ" />
</svelte:head>

<div id="chart"></div>

<svelte:window on:resize={resize} />
<!-- {JSON.stringify(beeData)} -->
<!-- <div class="pt-2 p-4" /> -->

<div class="box-border h-full w-full">
  <div
    class="mx-auto mb-4 flex flex-col justify-between rounded-lg bg-white p-4 shadow shadow-tertiary-300 md:h-16 md:flex-row lg:w-5/6"
  >
    <h1 class=" text-2xl font-semibold">
      Váha {beehive?.name ? beehive?.name : "Loading..."}
    </h1>
    <div class="mt-4 flex flex-row gap-4 md:mt-0">
      <RouterLink url="/action" append>
        <Button text="Udalosti" />
      </RouterLink>
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
          title: "Úspešnosť prenosu dát",
          spanX: 1,
          spanY: 1,
          editing: false,
          data: [
            {
              unit: "%",
              data: shared
                .getBeehiveById(props.id)
                .getTransmissionSuccessRate(),
              type: "successRate",
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
                beehive_id: [props.id],
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
                beehive_id: [props.id],
              },
            ],
          }}
        />

        {#each beehive.getCurrentDataTypes(true) as type}
          <!--{type}-->
          <EChart
            className=" col-span-2 row-span-1"
            cardStates={{
              mode: "static",
              editing: false,
              title: type + " váhy",
              data: [
                {
                  type: type,
                  beehive_id: [props.id],
                },
              ],
            }}
          />
        {/each}

        {#if !small && beehive.getCurrentDataTypes(true).length % 2 === 1}
          <div
            class="relative col-span-2 row-span-1 flex items-center justify-center rounded-md outline outline-1 outline-tertiary-500"
          >
            <div
              class="absolute left-4 top-4 aspect-square h-[25%] bg-contain bg-center bg-no-repeat"
              style="background-image: url('/img/bees/beeRight.png')"
            ></div>
            <!-- chart space-->
            <div class="box-border flex h-full w-full p-4">
              <svg
                class="flex-1"
                viewBox="0 0 178 67"
                fill="none"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  d="M1 55.5L10 62L22 43L27.5 49.5L42 30L59 48L72.5 37L78.5 43L94 49.5L98.5 57.5L104.5 65.5L114.5 55.5L120 45H132.5L144 30L149 34C152.167 31.8333 158.8 27.1 160 25.5C161.2 23.9 164.167 18.8333 165.5 16.5L171.5 7L177.5 1"
                  stroke="#939598"
                  stroke-width="0.5"
                />
              </svg>
            </div>

            <div
              class="absolute bottom-4 right-4 aspect-square h-[25%] -scale-x-100 bg-contain bg-center bg-no-repeat"
              style="background-image: url('/img/bees/beeRight.png')"
            ></div>
          </div>
        {/if}
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
