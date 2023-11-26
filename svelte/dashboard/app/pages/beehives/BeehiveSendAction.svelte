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

  fetchPendingActions();

  let li = getLanguageInstance();
</script>

<SettingsHeader title="Zaslať akcie" />

<SettingsItem>
  <Button text="Sledovanie akcií zariadenia" onClick={sendAction} />
</SettingsItem>

{#if Object.keys(pendingActions).length > 0}
  {#each Object.entries(pendingActions) as [id, action]}
    <ActionCard actionObject={action}></ActionCard>
  {/each}
{:else}
  <p>No pending actions found.</p>
{/if}
