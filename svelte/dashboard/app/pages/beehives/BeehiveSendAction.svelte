<script>
  import Button from "../../../../components/Buttons/Button.svelte";
  import message from "../../stores/message";
  import SettingsHeader from "../../component/settings/SettingsHeader.svelte";
  import {getLanguageInstance,} from "../../../../components/language/languageRepository";
  import shared, {onLoad} from "../../stores/shared";
  import ActionCard from "../../component/beehives/ActionCard.svelte";
  import Modal from "../../../../components/Modal.svelte";
  import staticFuncs, {generateUUID} from "../../../../components/lib/utils/staticFuncs";
  import DropdownInput from "../../../../components/Inputs/DropdownInput.svelte";

  export let props;

  const beehiveId = props.id;

  let beehiveObject = null;

  message.setMessage("Udalosti");

  let pendingActions = {};
  let actionOptions = [];

  const fetchPendingActions = async () => {
    try {
      const response = await fetch(
        `/actions/getPending?beehiveId=${beehiveId}`,
      );
      pendingActions = await response.json();
    } catch (error) {
      console.error("Error fetching pending actions:", error);
    }

    try {
      // /actions/getActionOptions
      const response = await fetch(`/actions/getActionOptions/${beehiveId}`);
      actionOptions = (await response.json()).actions;
    } catch (error) {
      console.error("Error fetching action options:", error);
    }
  };

  let user;
  onLoad(["user"], (userObj) => {
    user = userObj;
  });

  onLoad(["beehives"], (_) => {
    beehiveObject = shared.getBeehiveById(beehiveId);
  });

  function sendAction() {
    const actionData = {
      author: user.id,
      type: "MOTOR_MOVE",
      beehive: beehiveId,
      params: '{"id": 8500}',
      executionTime: new Date().getTime() + 1000 * 60 * 60,
    };

    fetch("/actions/newAction", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(actionData),
    })
      .then((response) => response.json())
      .then((data) => {
        console.log("Success:", data);
        fetchPendingActions();
      })
      .catch((error) => {
        console.error("Error:", error);
      });
  }

  function deleteAction(action_id) {
    fetch(`/actions/deleteAction/${action_id}`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
        // Add any additional headers if needed
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
      })
      .then((data) => {
        console.log("Delete Action Successful:", data);
        fetchPendingActions();
      })
      .catch((error) => {
        console.error("Error deleting action:", error);
        // Handle the error here
      });
  }

  fetchPendingActions();

  let li = getLanguageInstance();

  let addNewAction = true;
  let formId = generateUUID();

  function handleSubmit(formData) {}

  // let devices = beehiveObject.getDevices();
  // for (const dev of devices) {
  //   if (dev.port !== null && dev.type === "MOTOR") {
  //     // TODO change later
  //   }
  // }
</script>

<SettingsHeader className="flex flex-row" title="Zaslať akcie">
  <div class="flex flex-row items-center justify-end">
    <Button
      text="Nová akcia"
      image="icons/add.svg"
      onClick={() => {
        addNewAction = true;
      }}
    ></Button>
  </div>
</SettingsHeader>

<!-- TODO reimplement adding new actions like moving a motor and stuff -->
<!-- working on it !! -->
<!--<SettingsItem>-->
<!--  <Button text="Sledovanie akcií zariadenia" onClick={sendAction} />-->
<!--</SettingsItem>-->

{#if Object.keys(pendingActions).length > 0}
  {#each Object.entries(pendingActions) as [id, action]}
    <ActionCard
      actionObject={action}
      onDeleteCard={() => {
        deleteAction(action.id);
      }}
    ></ActionCard>
  {/each}
{:else}
  <div
    class="m-auto mt-4 flex h-24 flex-row items-center justify-center lg:w-5/6"
  >
    <div
      class="h-24 w-20 bg-[url('/icons/person-running.svg')] bg-cover bg-no-repeat opacity-50"
    ></div>
  </div>
  <h1 class="my-4 text-center text-4xl font-bold text-slate-600">
    Žiadne prebiehajúce akcie!
  </h1>
  <h1 class="my-4 text-center text-base text-slate-600">
    Všetky akcie váhy sa zobrazia tu
  </h1>
{/if}

{#if beehiveObject}
  {JSON.stringify(beehiveObject.getDevices())}
{/if}

<Modal bind:showModal={addNewAction}>
  <h2 slot="header" class="text-2xl font-bold">
    {"Pridať novú akciu"}
  </h2>

  <form
    on:submit|preventDefault={handleSubmit}
    id="addNewAction-{formId}"
    method="POST"
    class="my-4 flex flex-col gap-4"
  >
    <!--    Type of the action-->

    <DropdownInput
      label="Typ akcie"
      name="action_type"
      value={"UPDATE_STATUS"}
      options={staticFuncs.arrayToKeyValuePairs(actionOptions)}
    />
    <!--      onChange={typeChanged}-->

    <!-- sensor to be acted upon-->
    <!-- make dynamically shown and dynamicaly filled with right options -->
    <DropdownInput
      label="Senzor"
      name="beehive_sensor"
      value={"MOTOR"}
      options={[
        ["MOTOR", "MOTOR"],
        ["NEMOTOR", "NEMOTOR"],
      ]}
    />

    <!-- value of the action -->
  </form>

  <button slot="footer" type="submit" form="cardRootForm{formId}">
    <Button
      slot="footer"
      type="confirm"
      autofocus
      onClick={() => {
        // saveSettings();
        // dialog.close();
      }}
      clickType="submit"
      text="Urob to!"
    ></Button>
  </button>
</Modal>
