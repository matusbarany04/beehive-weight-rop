<script>

  import Button from "../../../../components/Buttons/Button.svelte";
  import message from "../../stores/message";

  export let props;

  const beehiveId = props.id;

  message.setMessage("Ovládať váhu");

  let pendingActions = {};

  const fetchPendingActions = async () => {
    try {
      const response = await fetch(`/actions/getPending?beehiveId=${beehiveId}`);
      const data = await response.json();
      pendingActions = data;
    } catch (error) {
      console.error('Error fetching pending actions:', error);
    }
  };
  function sendAction() {
    const actionData = {
      type: "MOTOR_MOVE",
      execution_time: 5000, // Replace with your desired execution time
      params: JSON.stringify({ 
        id:8500
      }),
      beehive_id: beehiveId
    };

    fetch('/actions/newAction', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(actionData),
    })
      .then(response => response.json())
      .then(data => {
        console.log('Success:', data);
        fetchPendingActions()
      })
      .catch((error) => {
        console.error('Error:', error);
      });
  }
  fetchPendingActions();
</script>


<h1>Zaslať akcie</h1>


<Button text="Send" onClick={sendAction}></Button>

<label for="beehiveId">Beehive ID:</label>
<button on:click={fetchPendingActions}>Fetch Pending Actions</button>

{#if Object.keys(pendingActions).length > 0}
  <ul>
    {#each Object.entries(pendingActions) as [id, action]}
      <li key={id}>
        <p>{JSON.stringify(action, null, '\t')}</p>
        <hr>
      </li>
    {/each}
  </ul>
{:else}
  <p>No pending actions found.</p>
{/if}