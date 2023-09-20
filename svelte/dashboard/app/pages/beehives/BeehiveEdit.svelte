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
  import Sensor from "../../component/beehives/Sensor.svelte";
  import Button from "../../../../components/Buttons/Button.svelte";
  import Modal from "../../../../components/Modal.svelte";

  export let props;

  let beehive;
  let token;
  let sensorWindow;
  let locationResults;
  let connectionMode = "0";

  let intervals = [
    [10, "10min"],
    [30, "30min"],
    [60, "1h"],
    [120, "2h"],
    [180, "3h"],
    [300, "5h"],
    [600, "10h"],
  ];
  
  function addSensor(e) {
    sensorWindow = false;
    console.log(e.target);
  }

  onLoad(["beehives"], () => {
    beehive = shared.getBeehiveById(props.id);
  });

  message.setMessage("Nastavenia zariadenia");

  function searchLocation(e) {
    console.log(e.target.value);
    const query = e.target.value;
    locationResults = [];

    fetch(
      "https://geocoding-api.open-meteo.com/v1/search?count=10&name=" + query,
    )
      .then((r) => r.json())
      .then((response) => {
        console.log(response["results"]);
        for (let item of response["results"]) {
          locationResults.push(`${item["name"]} (${item["country"]})`);
          locationResults = locationResults;
        }
      });
  }
</script>

{#if beehive}
  <div class="flex w-full flex-row justify-center">
    <form class="w-5/6" action="/dashboardApi/saveBeehive" method="post" id="device_settings">
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
          results={locationResults}
          on:input={searchLocation}
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

      <div class="m-4 rounded-lg bg-white p-4">

        <div class="flex m-4 items-center">
          <h3 class="font-bold w-full">Senzory</h3>
          <Button clickType="button" type="secondary" text="+ Pridať" onClick={() => sensorWindow = true} />
        </div>


      </div>
      <div class="flex gap-2 justify-end m-4">
        <Button type="primary" formId="device_settings" text="Uložiť zmeny"/>
        <Button type="secondary" formId="device_settings" text="Zahodiť zmeny"/>
      </div>
    </form>
    
  </div>


  
  <Modal bind:showModal={sensorWindow}>
    <h2 slot="header" class="text-2xl font-bold">Pridať Senzor</h2>
    <div class="grid grid-cols-3 gap-3 my-5">
      <Sensor name="Hmotnosť" img="../../../icons/weight.svg" on:click={addSensor} />
      <Sensor name="Teplota" img="../../../icons/temp.svg" on:click={addSensor}/>
      <Sensor name="Teplota+Vlhkosť" img="../../../icons/humidity.svg" on:click={addSensor}/>
      <Sensor name="Svetlo" img="../../../icons/light.svg" on:click={addSensor}/>
      <Sensor name="Zvuk" img="../../../icons/sound.svg" on:click={addSensor}/>
    </div>
  </Modal>
{:else}
  <Loading />
{/if}
