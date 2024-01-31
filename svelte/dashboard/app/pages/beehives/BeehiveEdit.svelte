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
  import {
    navigate,
    setUnsavedData,
  } from "../../../../components/router/route.serv";
  import { onMount } from "svelte";
  import { writable } from "svelte/store";
  import { getLanguageInstance } from "../../../../components/language/languageRepository";
  import SimpleDialog from "../../../../components/SimpleDialog.svelte";

  export let props;
  const MODEL_WITH_GSM = "BBIMZ-A";

  let beehive;
  let token;
  let sensorWindow;
  let locationResults;
  let coordinateList;
  let oldInterval;
  let actions;
  let coordinates = { latitude: 0, longitude: 0 };
  let connectionMode = "GSM";
  let sensors = {};
  let showDialog;
  let dialogMsg;
  let dialogYesAction;

  let intervals = [
    [10, "10min"],
    [30, "30min"],
    [60, "1h"],
    [120, "2h"],
    [180, "3h"],
    [300, "5h"],
    [600, "10h"],
  ];

  const li = getLanguageInstance();

  let socket = new WebSocket(
    (location.protocol === "https:" ? "wss://" : "ws://") + location.host + "/websocket/connect",
  );

  socket.onmessage = (message) => {
    const action = JSON.parse(message.data);
    if (action.messageType === "UPDATE_DEVICE_CONFIG") {
      sensors = action.params.devices;
    }
  };

  message.setMessage("Nastavenia zariadenia");

  onMount(() => setUnsavedData(true));

  fetch("/actions/getPending?beehiveId=" + props.id)
    .then((r) => r.json())
    .then((response) => (actions = response));

  onLoad(["beehives"], () => {
    beehive = shared.getBeehiveById(props.id);
    oldInterval = beehive.interval;
    coordinates.latitude = beehive.latitude;
    coordinates.longitude = beehive.longitude;
    connectionMode = beehive["connectionMode"] + "";
    document.title = beehive.name + " - " + message.getMessage();

    showFutureValues();

    for (let device of beehive.getDevices()) {
      sensors[device["port"]] = Object.assign({}, device);
      delete sensors[device["port"]]["port"];
    }
  });

  function showFutureValues() {
    if (actions) {
      for (let id in actions) {
        let action = actions[id];
        let params = JSON.parse(action.params);
        if (params["wifi_ssid"]) beehive["wifiSSID"] = params["wifi_ssid"];
        if (params["interval"]) beehive.interval = params["interval"];
      }
    } else setTimeout(showFutureValues, 1000);
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

  function refreshSensorView() {
    fetch("/dashboardApi/getDeviceConfig?beehive=" + props.id)
      .then((r) => r.json())
      .then((response) => {
        updateSensorView(response.devices);
      });
  }

  function updateSensorView(devices) {
    let usedPorts = [];
    for (let device of devices) {
      let port = device["port"];
      usedPorts.push(port);
      if (!sensors[port] || sensors[port]["id"] !== device.id) {
        sensors[device["port"]] = device;
        delete sensors[device["port"]]["port"];
      }
    }

    for (let port in sensors) {
      if (!usedPorts.includes(port)) delete sensors[port];
    }
  }

  function yesNoDialog(messageType, action) {
    dialogMsg = li.get("beehives.settings." + messageType);
    showDialog = true;
    dialogYesAction = action;
  }

  function deleteData() {
    fetch("/dashboardApi/deleteAllBeehiveData?token=" + beehive.beehive_id, {
      method: "DELETE",
    })
      .then((r) => r.json())
      .then((response) => {
        if (response.status === "ok") {
          toast.push("Všetky namerané dáta boli úspešne odstránené");
        } else toast.push("Pri vymazaní dát sa vyskytla chyba", "error");
      });
  }

  function factoryReset() {
    fetch("/actions/newAction", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        type: "FACTORY_RESET",
        beehive: beehive.beehive_id,
        executionTime: 0,
      }),
    })
      .then((response) => response.json())
      .then((response) => {
        if (response.status === "ok")
          toast.push("Všetky nastavenia váhy budú obnovené na predvolené");
        else
          toast.push(`Nastala chyba pri vytváraní akcie (${response.status})`);
      })
      .catch((error) =>
        toast.push(`Nastala chyba pri vytváraní akcie (${error})`),
      );
  }

  function deleteBeehive() {
    fetch("/dashboardApi/deleteBeehive/" + beehive.beehive_id, {
      method: "DELETE",
    })
      .then((r) => r.json())
      .then((response) => {
        if (response.status === "ok") {
          window.location = "/dashboard/beehives";
          toast.push("Váha bola úspešne odstránená");
        } else if (response.status === "ERR_ITEM_NOT_FOUND")
          toast.push("Váha sa nenašla", "error");
        else toast.push("Vyskytla sa neznáma chyba", "error");
      });
  }
</script>

<svelte:head>
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
      <div class="m-4 rounded-lg bg-white p-4 shadow shadow-tertiary-300">
        <Input
          type="text"
          name="name"
          label={li.get("beehives.settings.beehive_name")}
          value={beehive.name}
          inline
          required
        />
        <Input
          type="text"
          name="location"
          label={li.get("beehives.settings.location")}
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
          label={li.get("beehives.settings.interval")}
          options={intervals}
          onChange={(newValue) => {
            beehive.interval = parseInt(newValue);
          }}
          bind:value={beehive.interval}
          inline
        />

        {#if beehive.interval !== oldInterval}
          <div class="flex items-center gap-2">
            <img
              class="h-4 w-4"
              src="../../../icons/warning.png"
              alt="warning"
            />{li.get("beehives.settings.changes_info")}
          </div>
        {/if}
      </div>

      <div class="m-4 rounded-lg bg-white shadow shadow-tertiary-300">
        <h3 class="p-4 font-bold">
          {li.get("beehives.settings.connection_mode")}
        </h3>

        {#if beehive.model === MODEL_WITH_GSM}
          <SelectableOption
            name={li.get("beehives.settings.cellular_network")}
            bind:selection={connectionMode}
            value="GSM"
          >
            <Input
              type="password"
              name="sim_password"
              label={li.get("beehives.settings.sim_password")}
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
          <Input
            type="password"
            name="wifi_password"
            label={li.get("beehives.settings.password")}
            inline
          />
        </SelectableOption>

        <SelectableOption
          name="Iná váha"
          bind:selection={connectionMode}
          value={li.get("beehives.settings.other_beehive")}
        >
          <div>Práve pripravujeme</div>
        </SelectableOption>
      </div>

      <div class="m-4 rounded-lg bg-white shadow shadow-tertiary-300">
        <div class="flex items-center p-4 pb-0">
          <h3 class="w-full font-bold">
            {li.get("beehives.settings.connected_devices")}
          </h3>
          <Button
            type="secondary"
            text={li.get("beehives.settings.refresh")}
            image="./../../icons/refresh.svg"
            preventDefault={true}
            onClick={refreshSensorView}
          />
        </div>
        <i class="ml-4">{li.get("beehives.settings.update_info")}</i>
        <div class="mt-4">
          <SensorView bind:devices={sensors} />
        </div>
      </div>

      <div class="m-4 rounded-lg bg-white shadow shadow-tertiary-300">
        <div
          class="cursor-pointer rounded-lg p-2 font-semibold hover:bg-slate-100"
          on:click={() => yesNoDialog("data_dialog", deleteData)}
        >
          {li.get("beehives.settings.delete_data")}
        </div>
        <div
          class="cursor-pointer rounded-lg p-2 font-semibold hover:bg-slate-100"
          on:click={() => yesNoDialog("reset_dialog", factoryReset)}
        >
          {li.get("beehives.settings.factory_reset")}
        </div>
        <div
          class="text-red cursor-pointer rounded-lg p-2 font-semibold hover:bg-slate-100"
          on:click={() => yesNoDialog("beehive_dialog", deleteBeehive)}
        >
          {li.get("beehives.settings.delete_beehive")}
        </div>
      </div>

      <div class="m-4 flex justify-end gap-2">
        <Button
          type="primary"
          formId="device_settings"
          text={li.get("beehives.settings.save_changes")}
        />

        <Button
          type="secondary"
          text={li.get("beehives.settings.discard_changes")}
          clickType="button"
          onClick={() => history.back()}
        />
      </div>
    </form>
  </div>

  <SimpleDialog
    bind:show={showDialog}
    message={dialogMsg}
    action={dialogYesAction}
    positiveButton="Áno"
    negativeButton="Nie"
  />
{:else}
  <Loading />
{/if}
