<script>
  import Button from "../../../../components/Buttons/Button.svelte";
  import message from "../../stores/message";
  import SettingsHeader from "../../component/settings/SettingsHeader.svelte";
  import SettingsItem from "../../component/settings/SettingsItem.svelte";
  import {
    getLanguageInstance,
    languages,
  } from "../../../../components/language/languageRepository";

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

  function sendAction() {
    const actionData = {
      type: "MOTOR_MOVE",
      executionTime: new Date().getTime() + 1000 * 60 * 60, // Replace with your desired execution time
      params: JSON.stringify({
        id: 8500,
      }),
      beehive_id: beehiveId,
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

<div class="min-h-48 lg:min-h-24 mx-auto mb-4 rounded-lg bg-white p-4 lg:w-5/6">
  <label for="beehiveId">Beehive ID:</label>
  <button on:click={fetchPendingActions}>Fetch Pending Actions</button>

  {#if Object.keys(pendingActions).length > 0}
    <ul>
      {#each Object.entries(pendingActions) as [id, action]}
        <li key={id}>
          {JSON.stringify(action)}
          <p class="mb-4 font-bold">{li.get(`actions.${action.type}`)}</p>
          <!--          .toISOString().split("T")[0]-->
          <p>{new Date(action.execution_time)}</p>
        
          {#each Object.entries(JSON.parse(action.params)) as [paramKey, paramValue]}
            <div>
              <strong>{paramKey}:</strong>
              {JSON.stringify(paramValue)}
            </div>
          {/each}
          <hr class="my-4" />
        </li>
      {/each}
    </ul>
  {:else}
    <p>No pending actions found.</p>
  {/if}
</div>
