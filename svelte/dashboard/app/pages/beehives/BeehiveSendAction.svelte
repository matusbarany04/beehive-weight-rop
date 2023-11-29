<script>
  import Button from "../../../../components/Buttons/Button.svelte";
  import message from "../../stores/message";
  import SettingsHeader from "../../component/settings/SettingsHeader.svelte";
  import SettingsItem from "../../component/settings/SettingsItem.svelte";
  import {
    getLanguageInstance,
    languages,
  } from "../../../../components/language/languageRepository";
  import { onLoad } from "../../stores/shared";
  import ActionCard from "../../component/beehives/ActionCard.svelte";

  export let props;

  const beehiveId = props.id;

  message.setMessage("Udalosti");

  let pendingActions = {};

  const fetchPendingActions = async () => {
    try {
      const response = await fetch(
        `/actions/getPending?beehiveId=${beehiveId}`,
      );
      const data = await response.json();
      pendingActions = data;
    } catch (error) {
      console.error("Error fetching pending actions:", error);
    }
  };

  let user;
  onLoad(["user"], (userObj) => {
    user = userObj;
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
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        // Add any additional headers if needed
      },
    })
      .then(response => {
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
      })
      .then(data => {
        console.log('Delete Action Successful:', data);
        fetchPendingActions()
      })
      .catch(error => {
        console.error('Error deleting action:', error);
        // Handle the error here
      });
  }

  fetchPendingActions();

  let li = getLanguageInstance();
</script>

<SettingsHeader title="Zaslať akcie" />

<!-- TODO reimplement adding new actions like moving a motor and stuff -->
<!--<SettingsItem>-->
<!--  <Button text="Sledovanie akcií zariadenia" onClick={sendAction} />-->
<!--</SettingsItem>-->

{#if Object.keys(pendingActions).length > 0}
  {#each Object.entries(pendingActions) as [id, action]}
    <ActionCard actionObject={action} onDeleteCard={()=>{
      deleteAction(action.id)
    }}></ActionCard>
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
