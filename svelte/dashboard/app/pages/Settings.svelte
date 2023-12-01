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
  import { onMount, tick } from "svelte";
  import {
    getLanguageInstance,
    languages,
  } from "../../../components/language/languageRepository";

  const li = getLanguageInstance();

  message.setMessage(li.get("settings.page_title"));

  let settings = null;

  // old original copy with no references
  let originalSettings = null;

  onLoad("settings", (settings_json) => {
    console.log("settings loaded ", settings_json);
    settings = JSON.parse(JSON.stringify(settings_json));
    originalSettings = JSON.parse(JSON.stringify(settings_json));
  });

  let saveEnabled = false;
  let userObject;
  // old and original user with no references
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
    tick().then(() => {
      saveEnabled = !staticFuncs.jsonFlatEqual(settings, originalSettings);
      if (userObject !== null && originalUser !== null) {
        let userChanged = !staticFuncs.jsonFlatEqual(userObject, originalUser);

        saveEnabled = saveEnabled || userChanged;
      }

      setUnsavedData(saveEnabled);
    });
  }

  triggerSave();

  /**
   * Saves new settings to the database
   */
  function saveSettings() {
    fetch("/dashboardApi/settings/updateBatch", {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
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

    fetch("/user/change/language", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ language: userObject["language"] }),
    })
      .then((response) => {
        originalUser = { ...userObject };
        saveEnabled = false;
        resetUnsavedData();
        toast.push("New language preferences saved!");

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
    // for (let key in originalSettings) {
    //   console.log(
    //     "restoring ",
    //     key,
    //     "from " + settings[key] + " to " + originalSettings[key],
    //   );
    //   settings[key] = originalSettings[key];
    // }
    // settings = { ...settings };

    settings = { ...originalSettings };
    saveEnabled = false;
    resetUnsavedData();
    triggerSave();
  }
</script>

<svelte:head>
  <title>Nastavenia</title>
  <meta name="Nastavenia" content="Nastavenia" />
</svelte:head>

<!-- for debug purposes -->

<!--<p>originalUser</p>-->
<!--<pre>-->
<!--  <code>-->
<!--    {JSON.stringify(originalUser, null, 2)}-->
<!--  </code>-->
<!--</pre>-->
<!--<br/>-->

<!--<p>userObject</p>-->
<!--<pre>-->
<!--  <code>-->
<!--    {JSON.stringify(userObject, null, 2)}-->
<!--  </code>-->
<!--</pre>-->

<div class="absolute right-0 top-0 z-50 flex w-min justify-end gap-3 p-4">
  <div class="flex gap-4">
    {#if saveEnabled}
      <Button
        text={li.get("settings.btn_cancel")}
        type="secondary"
        onClick={() => {
          restoreSettings();
        }}
      />
    {/if}
    <Button
      bind:enabled={saveEnabled}
      text={li.get("settings.btn_save")}
      type="confirm"
      onClick={() => {
        saveSettings();
      }}
    />
  </div>
</div>

<form class="h-full w-full">
  {#if settings && userObject}
    <SettingsHeader title={li.get("settings.account.title")} />

    <SettingsItem
      title={li.get("settings.account.name.title")}
      detail={li.get("settings.account.name.description")}
    >
      <input
        type="text"
        placeholder={li.get("settings.account.name.title")}
        bind:value={userObject["name"]}
        on:input={triggerSave}
        class="h-8 w-96 rounded-md border-2 border-slate-300 px-4"
      />
    </SettingsItem>

    <SettingsItem
      title={li.get("settings.account.email.title")}
      detail={li.get("settings.account.email.description")}
    >
      <p
        class="h-8 w-96 rounded-md border-2 border-slate-300 px-4 text-slate-500"
      >
        {userObject["email"]}
      </p>
    </SettingsItem>

    <SettingsItem
      title={li.get("settings.account.change_password.title")}
      detail=""
    >
      <Button
        text={li.get("settings.account.change_password.btn_password_change")}
        link="/settings/newpassword"
      ></Button>
    </SettingsItem>

    <SettingsItem
      detail={li.get("settings.account.language_preferences.title")}
    >
      <select
        class="mr-4 h-8 w-72 rounded-md border-2 border-slate-300 bg-white px-4"
        on:change={triggerSave}
        bind:value={userObject["language"]}
      >
        {#each Object.entries(languages) as [key, language]}
          <option
            value={key}
            on:change={triggerSave}
            selected={key === userObject["language"]}
          >
            {language}
          </option>
        {/each}
      </select>
    </SettingsItem>

    <SettingsHeader title={li.get("settings.notifications.title")} />

    <SettingsItem
      title={li.get("settings.notifications.send_to_email.title")}
      detail={li.get("settings.notifications.send_to_email.description")}
    >
      <Toggle
        bind:checked={settings["sendNotifications"]}
        action={triggerSave}
      />
    </SettingsItem>

    <SettingsItem
      title={li.get("settings.notifications.send_to_another_email.title")}
    >
      <Toggle
        bind:checked={settings["useUserLoginMail"]}
        action={triggerSave}
      />
    </SettingsItem>

    {#if settings["useUserLoginMail"] === true}
      <SettingsItem
        title={li.get("settings.notifications.email.title")}
        detail={li.get("settings.notifications.email.description")}
      >
        <!--                action={triggerSave()} when changes-->
        <input
          type="text"
          placeholder={li.get("settings.notifications.email.placeholder")}
          bind:value={settings["altMail"]}
          on:input={triggerSave}
          class="h-8 w-96 rounded-md border-2 border-slate-300 px-4"
        />
      </SettingsItem>
    {/if}

    <SettingsItem
      title={li.get("settings.notifications.dont_disturb.title")}
      detail={li.get("settings.notifications.dont_disturb.description")}
    >
      <div class="flex flex-col md:flex-row">
        <div class="flex flex-col">
          <label for="time"
            >{li.get("settings.notifications.dont_disturb.time_from")} :</label
          >
          <input
            class="mr-8 h-8 w-44 rounded-md border-2 border-slate-300 px-4"
            type="time"
            id="time-input"
            name="time"
            step="3600"
            min="00:00"
            max="23:59"
            on:input={triggerSave}
            bind:value={settings["dontDisturbFrom"]}
            pattern="[0-2][0-9]:[0-5][0-9]"
          />
        </div>
        <div class="flex flex-col">
          <label for="time"
            >{li.get("settings.notifications.dont_disturb.time_to")}:
          </label>

          <input
            class="h-8 w-44 rounded-md border-2 border-slate-300 px-4"
            type="time"
            id="time-input"
            name="time"
            step="3600"
            min="00:00"
            max="23:59"
            on:input={triggerSave}
            bind:value={settings["dontDisturbTo"]}
            pattern="[0-2][0-9]:[0-5][0-9]"
          />
        </div>
      </div>
    </SettingsItem>

    <SettingsHeader title={li.get("settings.notifications.humidity.title")}
    ></SettingsHeader>

    <SettingsItem
      title={li.get("settings.notifications.humidity.high_humidity.title")}
      detail={li.get(
        "settings.notifications.humidity.high_humidity.description",
      )}
    >
      <Toggle bind:checked={settings["highHumidity"]} action={triggerSave} />
    </SettingsItem>
    {#if settings["highHumidity"] === true}
      <SettingsItem
        detail={li.get(
          "settings.notifications.humidity.high_humidity.high_humidity_threshold",
        )}
      >
        <select
          class="mr-4 h-8 w-72 rounded-md border-2 border-slate-300 bg-white px-4"
          name="pets"
          on:change={triggerSave}
          bind:value={settings["highHumidityThreshold"]}
          id="pet-select"
        >
          <option value="">--Please choose an option--</option>
          {#each Array(11) as _, index (index)}
            <option
              value={index * 10}
              selected={settings["highHumidityThreshold"] === index * 10}
            >
              {index * 10}%
            </option>
          {/each}
        </select>
      </SettingsItem>
    {/if}

    <SettingsItem
      title={li.get("settings.notifications.humidity.low_humidity.title")}
      detail={li.get(
        "settings.notifications.humidity.low_humidity.description",
      )}
    >
      <Toggle bind:checked={settings["lowHumidity"]} action={triggerSave} />
    </SettingsItem>
    {#if settings["lowHumidity"] === true}
      <SettingsItem
        detail={li.get(
          "settings.notifications.humidity.low_humidity.low_humidity_threshold",
        )}
      >
        <select
          class="mr-4 h-8 w-72 rounded-md border-2 border-slate-300 bg-white px-4"
          on:change={triggerSave}
          bind:value={settings["lowHumidityThreshold"]}
        >
          <option value="">--Please choose an option--</option>
          {#each Array(11) as _, index (index)}
            <option
              value={index * 10}
              selected={settings["lowHumidityThreshold"] === index * 10}
            >
              {index * 10}%
            </option>
          {/each}
        </select>
      </SettingsItem>
    {/if}

    <SettingsHeader title={li.get("settings.notifications.weight.title")}
    ></SettingsHeader>

    <!--weight -->
    <SettingsItem
      title={li.get("settings.notifications.weight.high_weight_hive.title")}
      detail={li.get(
        "settings.notifications.weight.high_weight_hive.description",
      )}
    >
      <Toggle bind:checked={settings["heavyWeight"]} action={triggerSave} />
    </SettingsItem>
    {#if settings["heavyWeight"] === true}
      <SettingsItem
        detail={li.get(
          "settings.notifications.weight.high_weight_hive.high_weight_threshold",
        )}
      >
        <select
          class="mr-4 h-8 w-72 rounded-md border-2 border-slate-300 bg-white px-4"
          on:change={triggerSave}
          bind:value={settings["heavyWeightThreshold"]}
        >
          <option value="">--Please choose an option--</option>
          {#each Array(11) as _, index (index)}
            <option
              value={index * 10}
              selected={settings["heavyWeightThreshold"] === index * 10}
            >
              {index * 10}kg
            </option>
          {/each}
        </select>
      </SettingsItem>
    {/if}

    <SettingsItem
      title={li.get("settings.notifications.weight.light_weight_hive.title")}
      detail={li.get(
        "settings.notifications.weight.light_weight_hive.description",
      )}
    >
      <Toggle bind:checked={settings["lightWeight"]} action={triggerSave} />
    </SettingsItem>
    {#if settings["lightWeight"] === true}
      <SettingsItem
        detail={li.get(
          "settings.notifications.weight.light_weight_hive.light_weight_threshold",
        )}
      >
        <select
          class="mr-4 h-8 w-72 rounded-md border-2 border-slate-300 bg-white px-4"
          name="pets"
          on:change={triggerSave}
          bind:value={settings["lightWeightThreshold"]}
          id="pet-select"
        >
          <option value="">--Please choose an option--</option>
          {#each Array(11) as _, index (index)}
            <option
              value={index * 10}
              selected={settings["lightWeightThreshold"] === index * 10}
            >
              {index * 10}kg
            </option>
          {/each}
        </select>
      </SettingsItem>
    {/if}

    <SettingsItem
      title={li.get("settings.notifications.battery.battery_low.title")}
      detail={li.get("settings.notifications.battery_battery_low.description")}
    >
      <select
        class="mr-4 h-8 w-72 rounded-md border-2 border-slate-300 bg-white px-4"
        name="pets"
        on:change={triggerSave}
        id="low-battery-select"
      >
        <option value="">--Please choose an option--</option>
        <option value="50" selected={settings["batteryLowThreshold"] === 50}
          >50%
        </option>
        <option value="20" selected={settings["batteryLowThreshold"] === 20}
          >20%
        </option>
        <option value="10" selected={settings["batteryLowThreshold"] === 10}
          >10%
        </option>
        <option value="0" selected={settings["batteryLowThreshold"] === 0}
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
