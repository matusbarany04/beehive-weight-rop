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
  import SensorView from "../../component/beehives/SensorView.svelte";
  import toast from "../../../../components/Toast/toast";
  import { setUnsavedData } from "../../../../components/router/route.serv";
  import { onMount } from "svelte";
  import { writable } from "svelte/store";

  export let props;
  const MODEL_WITH_GSM = "BBIMZ-A";

  let beehive;
  let token;
  let sensorWindow;
  let locationResults;
  let coordinateList;
  let oldInterval;
  let coordinates = { latitude: 0, longitude: 0 };
  let connectionMode = "GSM";
  let sensors = {};

  let intervals = [
    [10, "10min"],
    [30, "30min"],
    [60, "1h"],
    [120, "2h"],
    [180, "3h"],
    [300, "5h"],
    [600, "10h"],
  ];

  message.setMessage("Nastavenia zariadenia");

  onMount(() => setUnsavedData(true));

  onLoad(["beehives"], () => {
    beehive = shared.getBeehiveById(props.id);
    oldInterval = beehive.interval;
    coordinates.latitude = beehive.latitude;
    coordinates.longitude = beehive.longitude;
    connectionMode = beehive["connectionMode"] + "";
    document.title = beehive.name + " - " + message.getMessage();
    fetchActions();

    for (let device of beehive.devices) {
      sensors[device["port"]] = device;
      delete sensors[device["port"]]["port"];
    }
  });

  function fetchActions() {
    fetch("/actions/getPending?beehiveId=" + props.id)
      .then((r) => r.json())
      .then((actions) => {
        console.log(beehive);
        for (let id in actions) {
          let action = actions[id];
          if (
            action.type === "CHANGE_BEEHIVE_CONFIG" &&
            (action.status === "PENDING" || action.status === "SENT")
          ) {
            if (action.params["wifi_ssid"])
              beehive["wifiSSID"] = action.params["wifi_ssid"];
            if (action.params["interval"])
              beehive.interval = action.params["interval"];
          }
        }
      });
  }

  function generateName(name) {
    if (!exist(name)) return name;
    let newName = name + " 1";
    for (let i = 2; exist(newName); i++) newName = name + " " + i;
    return newName;
  }

  function exist(name) {
    console.log(sensors);
    for (let connector in sensors) {
      console.log(connector);
      if (sensors[connector].name === name) return true;
    }
    return false;
  }

  function searchLocation(e) {
    console.log(e.target.value);
    const query = e.target.value;
    locationResults = [];
    coordinateList = [];

    fetch(
      "https://geocoding-api.open-meteo.com/v1/search?count=10&name=" + query,
    )
      .then((r) => r.json())
      .then((response) => {
        console.log(response["results"]);
        for (let item of response["results"]) {
          locationResults.push(`${item["name"]} (${item["country"]})`);
          coordinateList.push({
            latitude: item["latitude"],
            longitude: item["longitude"],
          });
          locationResults = locationResults;
          coordinateList = coordinateList;
        }
      });
  }

  function saveSettings(e) {
    let formData = new FormData(e.target);
    formData.append("token", props.id);
    formData.append("connectionMode", connectionMode);
    formData.append("sensors", JSON.stringify(sensors));

    fetch("/dashboardApi/saveDeviceSettings", {
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      method: "POST",
      body: new URLSearchParams(formData),
    })
      .then((r) => r.json())
      .then((response) => {
        if (response.status === "ok") toast.push("Zmeny boli uložené");
        else
          toast.push(
            `Pri ukladaní nastala chyba (${response.status})`,
            "error",
          );
      });
  }
</script>

<svelte:head>
  <title>Upravenie úľu</title>
  <meta
    name="Edit beehive"
    content="This page shows edit options for a particular beehive"
  />
</svelte:head>

{#if beehive}
  <div>Model: {beehive.model}</div>
  <div class="flex w-full flex-row justify-center">
    <form
      class="w-5/6"
      on:submit|preventDefault={saveSettings}
      id="device_settings"
      autocomplete="off"
    >
      <div class="m-4 rounded-lg bg-white p-4">
        <Input
          type="text"
          name="name"
          label="Názov váhy"
          value={beehive.name}
          inline
          required
        />
        <Input
          type="text"
          name="location"
          label="Poloha"
          value={beehive.location}
          results={locationResults}
          on:input={searchLocation}
          resultValues={coordinateList}
          bind:resultValue={coordinates}
          inline
        />
        <input type="hidden" name="latitude" value={coordinates.latitude} />
        <input type="hidden" name="longitude" value={coordinates.longitude} />
        <DropdownInput
          name="interval"
          label="Interval merania"
          options={intervals}
          bind:value={beehive.interval}
          inline
        />

        {#if beehive.interval !== oldInterval}
          <div class="flex items-center gap-2">
            <img
              class="h-4 w-4"
              src="../../../icons/warning.png"
              alt="warning"
            />Zmeny sa aplikujú až pri najbližšom prebudení váhy
          </div>
        {/if}
      </div>

      <div class="m-4 rounded-lg bg-white">
        <h3 class="p-4 font-bold">Spôsob pripojenia</h3>

        {#if beehive.model === MODEL_WITH_GSM}
          <SelectableOption
            name="Mobilná sieť"
            bind:selection={connectionMode}
            value="GSM"
          >
            <Input
              type="password"
              name="sim_password"
              label="Heslo na SIM kartu"
              value=""
              inline
            />
          </SelectableOption>
        {/if}

        <SelectableOption
          name="WiFi"
          bind:selection={connectionMode}
          value="WIFI"
        >
          <Input
            type="text"
            name="wifi_ssid"
            label="Názov WiFi siete (SSID)"
            value={beehive["wifiSSID"]}
            inline
          />
          <Input type="password" name="wifi_password" label="Heslo" inline />
        </SelectableOption>

        <SelectableOption
          name="Iná váha"
          bind:selection={connectionMode}
          value="OTHER_BEEHIVE"
        >
          <div>Práve pripravujeme</div>
        </SelectableOption>
      </div>

      <div class="m-4 rounded-lg bg-white p-4">
        <div class="m-4 flex items-center">
          <h3 class="w-full font-bold">Senzory</h3>
        </div>
        <SensorView bind:devices={sensors} />
      </div>
      <div class="m-4 flex justify-end gap-2">
        <Button type="primary" formId="device_settings" text="Uložiť zmeny" />

        <Button
          type="secondary"
          text="Zahodiť zmeny"
          clickType="button"
          onClick={() => history.back()}
        />
      </div>
    </form>
  </div>
{:else}
  <Loading />
{/if}
