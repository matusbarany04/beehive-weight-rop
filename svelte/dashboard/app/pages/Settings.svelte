<script>
  /**
   * @fileoverview This page handles general settings of the dashboard
   * @module Settings
   */
  import { onMount } from "svelte";
  import SettingsHeader from "../component/settings/SettingsHeader.svelte";
  import SettingsItem from "../component/settings/SettingsItem.svelte";
  import Toggle from "../component/settings/Toggle.svelte";
  import message from "../stores/message";

  message.setMessage("Nastavenia");
  
</script>

<svelte:head>
  <title>Analytika</title>
  <meta name="Analytika" content="Analytika" />
</svelte:head>

<form class="h-full w-full">
  <SettingsHeader title="Všeobecné" />
  <SettingsItem
    title="Email"
    detail="Email na ktorý sa budú zasielať upozornenia"
    inputPlaceholder="Váš email"
  >
    <input
      type="text"
      placeholder="Váš email"
      class="h-8 w-96 rounded-md border-2 border-slate-300 px-4"
    />
  </SettingsItem>
  <SettingsItem
    title="Nerušiť medzi"
    detail="Čas medzi ktorým sa nebudú posielať upozornenia"
    inputPlaceholder="Váš email"
  >
    <div class="flex flex-col md:flex-row">
      <div class="flex flex-col">
        <label for="time">čas od: </label>
        <input
          class="mr-8 h-8 w-44 rounded-md border-2 border-slate-300 px-4"
          type="time"
          id="time-input"
          name="time"
          step="3600"
          min="00:00"
          max="23:59"
          pattern="[0-2][0-9]:[0-5][0-9]"
        />
      </div>
      <div class="flex flex-col">
        <label for="time">čas do: </label>

        <input
          class="h-8 w-44 rounded-md border-2 border-slate-300 px-4"
          type="time"
          id="time-input"
          name="time"
          step="3600"
          min="00:00"
          max="23:59"
          pattern="[0-2][0-9]:[0-5][0-9]"
        />
      </div>
    </div>
  </SettingsItem>

  <SettingsHeader title="Upozornenia" />

  <SettingsItem
    title="Vlhkosť vzduchu"
    detail="Pri zvýšenej vlhkosti Vám príde upozornenie"
  >
    <Toggle />
  </SettingsItem>

  <SettingsItem
    title="Batéria úľov"
    detail="Pri nízkej báterií Vám príde upozornenie"
  >
    <select
      class="mr-4 h-8 w-72 rounded-md border-2 border-slate-300 bg-white px-4"
      name="pets"
      id="pet-select"
    >
      <option value="">--Please choose an option--</option>
      <option value="50%">50%</option>
      <option value="20">20%</option>
      <option value="10%">10%</option>
      <option value="0%">0%</option>
    </select>
    <Toggle />
  </SettingsItem>
</form>
