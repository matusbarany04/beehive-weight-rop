<script>

  /**
   * @fileoverview This page shows edit options for a particular beehive
   * @module BeehiveEdit
   */
  import SettingsItem from "../../component/settings/SettingsItem.svelte";
  import Input from "../../../../components/Inputs/Input.svelte";
  import shared, { onLoad } from "../../stores/shared";
  import Loading from "../../../../components/pages/Loading.svelte";
  import DropdownInput from "../../../../components/Inputs/DropdownInput.svelte";
  import SelectableOption from "../../../../components/Inputs/SelectableOption.svelte";
  import message from "../../stores/message";

  //import props from "./Beehive.svelte";

  let beehive;
  let token;

  let connectionMode = "0";

  export let props;


  let intervals = [
    [10, "10min"],
    [30, "30min"],
    [60, "1h"],
    [120, "2h"],
    [180, "3h"],
    [300, "5h"],
    [600, "10h"],
  ];

  onLoad(["beehives"], () => {
    beehive = shared.getBeehiveById(props.id);
  });

  message.setMessage("Nastavenia zariadenia");

</script>

{#if beehive}
  <div class="flex w-full flex-row justify-center">
    <form class="w-5/6" action="/dashboardApi/saveBeehive" method="post">
      <div class="m-4 rounded-lg bg-white p-4">
        <Input
          type="text"
          name="title"
          label="Názov váhy"
          value={beehive.name}
          inline
          required=""
        />
        <Input
          type="text"
          name="location"
          label="Poloha"
          value={beehive.location}
          inline
        />
        <DropdownInput
          name="interval"
          label="Interval merania"
          options={intervals}
          value={beehive.interval}
          inline
        />
      </div>

      <div class="m-4 rounded-lg bg-white">
        <h3 class="p-4 font-bold">Spôsob pripojenia</h3>

        <SelectableOption
          name="Mobilná sieť"
          bind:selection={connectionMode}
          value="0"
        >
          <Input
            type="password"
            name="sim_password"
            label="Heslo na SIM kartu"
            value="0000"
            inline
          />
        </SelectableOption>

        <SelectableOption name="WiFi" bind:selection={connectionMode} value="1">
          <Input
            type="text"
            name="wifi_ssid"
            label="Názov WiFi siete (SSID)"
            inline
          />
          <Input type="password" name="wifi_password" label="Heslo" inline />
        </SelectableOption>

        <SelectableOption
          name="Iná váha"
          bind:selection={connectionMode}
          value="2"
        >
          <div>Práve pripravujeme</div>
        </SelectableOption>
      </div>
    </form>
  </div>
{:else}
  <Loading />
{/if}
