<script>
  /**
   * @fileoverview This page handles general settings of the dashboard
   * @module Settings
   */

  import {draw, slide} from 'svelte/transition';
  import SettingsHeader from "../component/settings/SettingsHeader.svelte";
  import SettingsItem from "../component/settings/SettingsItem.svelte";
  import message from "../stores/message";
  import shared, {onLoad} from "../stores/shared";
  import Loading from "../../../components/pages/Loading.svelte";
  import Button from "../../../components/Buttons/Button.svelte";
  import {
    setUnsavedData,
  } from "../../../components/router/route.serv";
  import {getParamValue, removeParam} from "../../../components/router/route.serv";

  message.setMessage("Nastavenia");

  let saveEnabled = false;
  let userObject;

  import {onMount} from 'svelte';
  import toast from "../../../components/Toast/toast";

  onMount(() => {
    // Check the 'passwordChanged' parameter and toast the appropriate message
    if (getParamValue('passwordChanged') === 'true') {
      toast.push('Password changed successfully!', "default");
    } else if (getParamValue('passwordChanged') === 'false') {
      toast.push('Failed to change password', "error");
    }
    removeParam("passwordChanged")
  });

  onLoad("user", (user) => {
    userObject = user;
  });

  let newPassword = "";
  let oldPassword = "";

  let validationError = '';
  let error = {
    oldPassword: false,
    newPassword: false
  }

  /**
   * Function that updates save button enabled state when some setting changes
   * @return void
   */
  function triggerSave() {
    saveEnabled = oldPassword.length > 0 && newPassword.length > 0;
    if (saveEnabled) {
      validationError = ''
    }

  }

  triggerSave();

  function validateForm() {
    if (!oldPassword) {
      validationError = 'Current password is required.';
      error.oldPassword = true;
      return false;
    }

    // Validate new password length
    if (newPassword.length < 8) {
      validationError = 'New password should be at least 8 characters long.';
      error.newPassword = true;
      return false;
    }

    error = {...error};
    validationError = '';  // Reset validation error
    return true;
  }

  function handleSubmit(event) {
    if (!validateForm()) {
      saveEnabled = false;
      event.preventDefault();
    }
  }

</script>

<svelte:head>
  <title>Analytika</title>
  <meta name="Analytika" content="Analytika"/>
</svelte:head>

<form class="h-full w-full" action="/changePassword" method="post" on:submit={handleSubmit}>
  {#if userObject}
    <SettingsHeader title="Zmena hesla"/>
    <input type="email" value="{userObject.email}" name="email" class="hidden">
    {#if validationError}
      <div
        out:slide={{ duration: 500 }}
        in:slide={{ duration: 500 }}
        class="min-h-48 lg:min-h-24 mx-auto mb-4 flex flex-col justify-between rounded-lg bg-white p-4 lg:w-5/6 lg:flex-row lg:justify-center">
          <p class="text-error-500 overflow-hidden">{validationError}</p>
      </div>
    {/if}
    <SettingsItem
      title="Vaše staré heslo"
      detail="Pre zmenu hesla zadajte prosím Vaše staré heslo"
    >
      <input
        type="password"
        placeholder="Staré heslo"
        name="currentPassword"
        bind:value={oldPassword}
        on:input={triggerSave}
        class="{error.oldPassword ? 'border-error-500 ' : 'border-slate-300'} h-8 w-96 rounded-md border-2  px-4"
      />
    </SettingsItem>

    <SettingsItem
      title="Vaše nové heslo"
      detail="S týmto heslom sa budete prihlasovať do Vašeho účtu"
    >
      <input
        name="newPassword"
        type="password"
        placeholder="Nové heslo"
        bind:value={newPassword}
        on:input={triggerSave}
        class="{error.newPassword ? 'border-error-500 ' : 'border-slate-300'} h-8 w-96 rounded-md border-2 px-4"
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
        clickType="{saveEnabled ? 'submit' : 'button' }"
      />
      <!-- 
      onClick={() => {
        if(saveEnabled){
          changePassword();
        }
      }}-->
    </div>
  {:else}
    <Loading/>
  {/if}
</form>
