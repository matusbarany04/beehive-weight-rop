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

  message.setMessage("Nastavenia");

  let settings = null;
  let originalSettings = null;

  onLoad("settings", (settings_json) => {
    settings = { ...settings_json };
    originalSettings = { ...settings_json };
  });

  let saveEnabled = false;
  let userObject;
  let originalUser;
  onLoad("user", (user) => {
    userObject = { ...user };
    originalUser = { ...user };
  });

  /**
   * Function that updates save button enabled state when some setting changes
   * @return void
   */
  function triggerSave() {
    saveEnabled = !staticFuncs.jsonFlatEqual(settings, originalSettings);
    console.log("save", saveEnabled, settings, originalSettings);

    if (userObject && originalUser) {
      console.log("user", userObject["name"], originalUser["name"]);
      saveEnabled = saveEnabled || userObject["name"] !== originalUser["name"];
    }

    setUnsavedData(saveEnabled);
  }

  triggerSave();

  /**
   * Saves new settings to the database
   */
  function changePassword() {}
</script>

<svelte:head>
  <title>Analytika</title>
  <meta name="Analytika" content="Analytika" />
</svelte:head>

<form class="h-full w-full">
  {#if settings}
    <SettingsHeader title="Zmena hesla" />

    <SettingsItem
      title="Vaše staré heslo"
      detail="Pre zmenu hesla zadajte prosím Vaše staré heslo"
    >
      <input
        type="password"
        placeholder="Staré heslo"
        bind:value={userObject["name"]}
        on:input={triggerSave}
        class="h-8 w-96 rounded-md border-2 border-slate-300 px-4"
      />
    </SettingsItem>

    <SettingsItem
      title="Vaše nové heslo"
      detail="S týmto heslom sa budete prihlasovať do Vašeho účtu"
    >
      <input
        type="password"
        placeholder="Nové heslo"
        bind:value={userObject["name"]}
        on:input={triggerSave}
        class="h-8 w-96 rounded-md border-2 border-slate-300 px-4"
      />
    </SettingsItem>
    <div
      class="min-h-48 lg:min-h-24 relative mx-auto mb-4 flex sm:block lg:w-5/6"
    >
      <Button
        className="float-right sm:w-16"
        bind:enabled={saveEnabled}
        text={"Uložiť"}
        type="confirm"
        onClick={() => {
          saveSettings();
        }}
      />
    </div>
  {:else}
    <Loading />
  {/if}
</form>
