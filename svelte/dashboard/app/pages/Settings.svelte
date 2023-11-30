<script>
  /**
   * @fileoverview This page handles general settings of the dashboard
   * @module Settings
   */
  import SettingsHeader from "../component/settings/SettingsHeader.svelte";
  import SettingsItem from "../component/settings/SettingsItem.svelte";
  import Toggle from "../component/settings/Toggle.svelte";
  import message from "../stores/message";
  import shared, { onLoad } from "../stores/shared";
  import Loading from "../../../components/pages/Loading.svelte";
  import staticFuncs, {
    generateUUID,
  } from "../../../components/lib/utils/staticFuncs";
  import Button from "../../../components/Buttons/Button.svelte";
  import toast from "../../../components/Toast/toast";
  import {
    setUnsavedData,
    resetUnsavedData,
  } from "../../../components/router/route.serv";
  import { onMount } from "svelte";
  import { nextTick } from "process";

  message.setMessage("Nastavenia");

  let settings = null;
  let originalSettings = null;

  onLoad("settings", (settings_json) => {
    console.log("settings loaded ", settings_json);
    settings = JSON.parse(JSON.stringify(settings_json));
    originalSettings = JSON.parse(JSON.stringify(settings_json));
  });

  let saveEnabled = false;
  let userObject;
  let originalUser;
  onLoad("user", (user) => {
    userObject = JSON.parse(JSON.stringify(user));
    originalUser = JSON.parse(JSON.stringify(user));
  });

  /**
   * Function that updates save button enabled state when some setting changes
   * @return void
   */
  function triggerSave() {
    saveEnabled = !staticFuncs.jsonFlatEqual(settings, originalSettings);

    if (userObject && originalUser) {
      saveEnabled = saveEnabled || userObject["name"] !== originalUser["name"];
    }

    setUnsavedData(saveEnabled);
  }

  triggerSave();

  /**
   * Saves new settings to the database
   */
  function saveSettings() {
    fetch("/dashboardApi/settings/updateBatch", {
      method: "PUT",
      body: JSON.stringify(settings),
    })
      .then((response) => {
        if (response.ok) {
          originalSettings = { ...settings };
          saveEnabled = false;
          resetUnsavedData();
          toast.push("Settings saved!");

          shared.fetchUser();
        } else {
          toast.push("Settings probably not saved ... ");
        }
      })
      .catch((error) => {
        toast.push(
          "Something happened when saving notification settings",
          "error",
        );
      });

    fetch("/user/change/username", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ name: userObject["name"] }),
    })
      .then((response) => {
        originalUser = { ...userObject };
        saveEnabled = false;
        resetUnsavedData();
        toast.push("User settings saved!");

        shared.fetchUser();
      })
      .catch((error) => {
        toast.push("Something happened when saving user", "error");
      });
  }

  onMount(() => {});

  function restoreSettings() {
    for (let key in originalUser) {
      userObject[key] = originalUser[key];
    }
    userObject = { ...userObject };

    // Update properties of settings
    for (let key in originalSettings) {
      console.log(
        "restoring ",
        key,
        "from " + settings[key] + " to " + originalSettings[key],
      );
      settings[key] = originalSettings[key];
    }
    settings = { ...settings };

    saveEnabled = false;
    resetUnsavedData();
    triggerSave();
  }
</script>

<svelte:head>
  <title>Nastavenia</title>
  <meta name="Analytika" content="Analytika" />
</svelte:head>

<div class="absolute right-0 top-0 z-50 flex w-min justify-end gap-3 p-4">
  <div class="flex gap-4">
    {#if saveEnabled}
      <Button
        text={"Zrušiť"}
        type="secondary"
        onClick={() => {
          restoreSettings();
        }}
      />
    {/if}
    <Button
      bind:enabled={saveEnabled}
      text={"Uložiť"}
      type="confirm"
      onClick={() => {
        saveSettings();
      }}
    />
  </div>
</div>

<form class="h-full w-full">
  {#if settings && userObject}
    <SettingsHeader title="Účet" />

    <SettingsItem
      title="Vaše meno"
      detail="Pod týmto menom Vás budeme oslovovať v e-mailoch a na webovej stránke"
    >
      <input
        type="text"
        placeholder="Vaše meno"
        bind:value={userObject["name"]}
        on:input={triggerSave}
        class="h-8 w-96 rounded-md border-2 border-slate-300 px-4"
      />
    </SettingsItem>

    <SettingsItem
      title="Váš email"
      detail="Email, ktorý ste použili pri registrácií"
    >
      <p
        class="h-8 w-96 rounded-md border-2 border-slate-300 px-4 text-slate-500"
      >
        {userObject["email"]}
      </p>
    </SettingsItem>

    <SettingsItem title="Zmeniť heslo" detail="">
      <Button text="Zmeniť heslo" link="/settings/newpassword"></Button>
    </SettingsItem>

    <SettingsHeader title="Upozornenia" />

    <SettingsItem
      title="Posielať upozornenia na mail"
      detail="Týmto nastavením zapnete/vypnete všetky nastavenia"
    >
      <Toggle
        bind:checked={settings["send_notifications"]}
        action={triggerSave}
      />
    </SettingsItem>

    <SettingsItem title="Posielať upozornenia iný mail">
      <Toggle
        bind:checked={settings["use_user_login_mail"]}
        action={triggerSave}
      />
    </SettingsItem>

    {#if settings["use_user_login_mail"] === true}
      <SettingsItem
        title="Email"
        detail="Email na ktorý sa budú zasielať upozornenia"
        inputPlaceholder="Váš email"
      >
        <!--                action={triggerSave()} when changes-->
        <input
          type="text"
          placeholder="Váš email"
          bind:value={settings["alt_mail"]}
          on:input={triggerSave}
          class="h-8 w-96 rounded-md border-2 border-slate-300 px-4"
        />
      </SettingsItem>
    {/if}

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
            on:input={triggerSave}
            bind:value={settings["dont_disturb_from"]}
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
            on:input={triggerSave}
            bind:value={settings["dont_disturb_to"]}
            pattern="[0-2][0-9]:[0-5][0-9]"
          />
        </div>
      </div>
    </SettingsItem>

    <SettingsHeader title="Vlhkosť"></SettingsHeader>

    <SettingsItem
      title="Zvýšená vlhkosť vzduchu"
      detail="Pri zvýšenej vlhkosti Vám príde upozornenie"
    >
      <Toggle bind:checked={settings["high_humidity"]} action={triggerSave} />
    </SettingsItem>
    {#if settings["high_humidity"] === true}
      <SettingsItem detail="High humidity Threshold">
        <select
          class="mr-4 h-8 w-72 rounded-md border-2 border-slate-300 bg-white px-4"
          name="pets"
          on:select={triggerSave}
          bind:value={settings["high_humidity_threshold"]}
          id="pet-select"
        >
          <option value="">--Please choose an option--</option>
          {#each Array(11) as _, index (index)}
            <option
              value={index * 10}
              selected={settings["high_humidity_threshold"] === index * 10}
            >
              {index * 10}%
            </option>
          {/each}
        </select>
      </SettingsItem>
    {/if}

    <SettingsItem
      title="Znížená vlhkosť vzduchu"
      detail="Pri zníženej vlhkosti Vám príde upozornenie"
    >
      <Toggle bind:checked={settings["low_humidity"]} action={triggerSave} />
    </SettingsItem>
    {#if settings["low_humidity"] === true}
      <SettingsItem detail="Low humidity Threshold">
        <select
          class="mr-4 h-8 w-72 rounded-md border-2 border-slate-300 bg-white px-4"
          name="pets"
          on:select={triggerSave}
          bind:value={settings["low_humidity_threshold"]}
          id="pet-select"
        >
          <option value="">--Please choose an option--</option>
          {#each Array(11) as _, index (index)}
            <option
              value={index * 10}
              selected={settings["low_humidity_threshold"] === index * 10}
            >
              {index * 10}%
            </option>
          {/each}
        </select>
      </SettingsItem>
    {/if}

    <SettingsHeader title="Váha"></SettingsHeader>

    <!--weight -->
    <SettingsItem
      title="Vysoká váha úľa"
      detail="Pri zvýšenej váhe Vám príde upozornenie"
    >
      <Toggle bind:checked={settings["heavy_weight"]} action={triggerSave} />
    </SettingsItem>
    {#if settings["heavy_weight"] === true}
      <SettingsItem detail="Heavy weight threshold">
        <select
          class="mr-4 h-8 w-72 rounded-md border-2 border-slate-300 bg-white px-4"
          name="pets"
          on:select={triggerSave}
          bind:value={settings["heavy_weight_threshold"]}
          id="pet-select"
        >
          <option value="">--Please choose an option--</option>
          {#each Array(11) as _, index (index)}
            <option
              value={index * 10}
              selected={settings["heavy_weight_threshold"] === index * 10}
            >
              {index * 10}kg
            </option>
          {/each}
        </select>
      </SettingsItem>
    {/if}

    <SettingsItem
      title="Znížená váha úľa"
      detail="Pri zníženej váhe Vám príde upozornenie"
    >
      <Toggle bind:checked={settings["light_weight"]} action={triggerSave} />
    </SettingsItem>
    {#if settings["light_weight"] === true}
      <SettingsItem detail="Low weight threshold">
        <select
          class="mr-4 h-8 w-72 rounded-md border-2 border-slate-300 bg-white px-4"
          name="pets"
          on:select={triggerSave}
          bind:value={settings["light_weight_threshold"]}
          id="pet-select"
        >
          <option value="">--Please choose an option--</option>
          {#each Array(11) as _, index (index)}
            <option
              value={index * 10}
              selected={settings["light_weight_threshold"] === index * 10}
            >
              {index * 10}kg
            </option>
          {/each}
        </select>
      </SettingsItem>
    {/if}

    <SettingsItem
      title="Batéria úľov"
      detail="Pri nízkej báterií Vám príde upozornenie"
    >
      <select
        class="mr-4 h-8 w-72 rounded-md border-2 border-slate-300 bg-white px-4"
        name="pets"
        on:select={triggerSave}
        id="low-battery-select"
      >
        <option value="">--Please choose an option--</option>
        <option value="50" selected={settings["battery_low_threshold"] === 50}
          >50%
        </option>
        <option value="20" selected={settings["battery_low_threshold"] === 20}
          >20%
        </option>
        <option value="10" selected={settings["battery_low_threshold"] === 10}
          >10%
        </option>
        <option value="0" selected={settings["battery_low_threshold"] === 0}
          >0%
        </option>
      </select>
      <Toggle />
    </SettingsItem>
  {:else}
    {#each Array.from({ length: 3 }) as _, i}
      <div
        class="loading mx-auto mb-4 flex h-24 flex-col justify-between rounded-md bg-tertiary-200 p-4 lg:w-5/6"
      ></div>
      <div
        class="loading mx-auto mb-4 flex h-48 flex-col justify-between rounded-md bg-tertiary-200 p-4 lg:w-5/6"
      ></div>
    {/each}
  {/if}
</form>

<style>
  .loading {
    animation: flash 3s infinite;
  }

  @keyframes flash {
    0%,
    100% {
      opacity: 0.5;
    }
    25%,
    75% {
      opacity: 1;
    }
  }
</style>
