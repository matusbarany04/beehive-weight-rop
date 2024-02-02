<script>
  /**
   * TODO add time input to specify execution time of the action
   */

  import Button from "../../../../components/Buttons/Button.svelte";
  import message from "../../stores/message";
  import SettingsHeader from "../../component/settings/SettingsHeader.svelte";
  import {getLanguageInstance} from "../../../../components/language/languageRepository";
  import shared, {onLoad} from "../../stores/shared";
  import ActionCard from "../../component/beehives/ActionCard.svelte";
  import Modal from "../../../../components/Modal.svelte";
  import staticFuncs, {
    generateUUID,
  } from "../../../../components/lib/utils/staticFuncs";
  import DropdownInput from "../../../../components/Inputs/DropdownInput.svelte";
  import Input from "../../../../components/Inputs/Input.svelte";
  import toast from "../../../../components/Toast/toast";
  import {writable} from "svelte/store"

  export let props;

  const beehiveId = props.id;
  /** @type {BeehiveObj} */
  let beehiveObject = null;

  message.setMessage("Udalosti");

  let pendingActions = {};
  let actionOptions = [];
  let chosenAction = "";
  let dictionary = {};
  let templates = {};
  let currentTemplate = {};

  const fetchPendingActions = async () => {
    try {
      const response = await fetch(
        `/actions/getPending?beehiveId=${beehiveId}`,
      );
      pendingActions = await response.json();
      // may be error exception out of bounds
      chosenAction = pendingActions[0];
    } catch (error) {
      console.error("Error fetching pending actions:", error);
    }

    try {
      // /actions/getActionOptions
      const response = await fetch(`/actions/getActionOptions/${beehiveId}`);
      const responseJson = await response.json();

      console.log("response ActionOptions", responseJson);
      actionOptions = responseJson.actions;
      dictionary = responseJson.dictionary;
      templates = responseJson.template;
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

  $: loaded = writable({})

  function waitForActionResult(actionId) {
    console.log("wait for action ! ")
    $loaded[actionId] = {
      status: "loading"
    };
    let socket = new WebSocket(
      (location.protocol === "https:" ? "wss://" : "ws://") +
      location.host +
      "/websocket/connect",
    );

    socket.onmessage = (message) => {
      const action = JSON.parse(message.data);
      if (
        action.messageType === "ACTION_RESULT" &&
        action.params.id === actionId
      ) {
        socket.close();

        $loaded[actionId] = {
          status: "finished"
        };
        console.log("finished")
        $loaded = {...loaded};
      } else {
        $loaded[actionId] = {
          status: "failed"
        };
        console.log("failed")
        
      }
    };
  }

  function sendAction(type, params) {
    const actionData = {
      author: user.id,
      type: type,
      beehive: beehiveId,
      params: params,
      executionTime: 0,
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
        console.log("Success:", data, data.action.id);

        //add check if online stuff 
        waitForActionResult(data.action.id)

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

  let addNewAction = false;
  let formId = generateUUID();

  function handleSubmit(event) {
    console.log("handling submit");
    event.preventDefault(); // Prevent the default form submission

    const data = new FormData(this);
    const actionType = data.get("action_type");
    const formDataObject = {};

    // Iterate over form data and build the formDataObject
    data.forEach((value, key) => {
      if (key.startsWith("dynamic_")) {
        let returnValue = value;
        const paramName = key.replace("dynamic_", "");

        if (currentTemplate[paramName] === "NUMERIC") {
          if (typeof returnValue === "string") {
            returnValue = parseInt(returnValue, 10);
          } else {
            console.warn(
              `Value for ${paramName} is not a string and cannot be converted to a number.`,
            );
          }
        } else if (currentTemplate[paramName] === "BOOLEAN") {
          if (typeof returnValue === "string") {
            // Convert string "true" or "false" to boolean
            returnValue = returnValue.toLowerCase() === "true";
          } else {
            console.warn(
              `Value for ${paramName} is not a string and cannot be converted to a boolean.`,
            );
          }
        }

        formDataObject[paramName] = returnValue;
      }
    });

    if (data.get("sensorId"))
      formDataObject.sensorId = parseInt(data.get("sensorId"));

    if (Object.keys(formDataObject).length > 0) {
      sendAction(actionType, JSON.stringify(formDataObject));
    } else {
      sendAction(actionType, JSON.stringify({}));
    }

    addNewAction = false;
  }

  // let devices = beehiveObject.getDevices();
  // for (const dev of devices) {
  //   if (dev.port !== null && dev.type === "MOTOR") {
  //     // TODO change later
  //   }
  // }

  let sensorNeeded = false;
  let sensorDropdownOptions = [];

  let dataOptions = [
    ["1", "on"],
    ["0", "off"],
  ];
  let paramsNeeded = false;

  let typeChanged = (value) => {
    chosenAction = value;
    if (templates[value] !== null) {
      currentTemplate = templates[value];
    }

    if (dictionary[value] != null) {
      console.log("activate second dropdown!");
      sensorNeeded = true;
      paramsNeeded = true;
      let deviceOptions = beehiveObject.getDevicesByType(dictionary[value]);

      sensorDropdownOptions = deviceOptions.map((device) => [
        device.id,
        device.name,
      ]);
    } else {
      console.log("deactivate second dropdown!");
      sensorNeeded = false;
      paramsNeeded = false;
    }
  };
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
{JSON.stringify($loaded)}
{#if Object.keys(pendingActions).length > 0}
  {#each Object.values(pendingActions).reverse() as action (action.id)}
    <ActionCard
      key={action.id}
      loading={$loaded[action.id]?.status === "loading"}
      finished={$loaded[action.id]?.status === "finished"}
      failed={$loaded[action.id]?.status === "failed"}
      actionObject={action}
      onDeleteCard={() => {
        deleteAction(action.id);
      }}
    ></ActionCard>
  {/each}
{:else}
  <!--TODO adda translation -->
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
      onChange={typeChanged}
    />

    <!-- sensor to be acted upon-->
    <!-- TODO make dynamically shown and dynamically filled with right options -->
    {#if sensorNeeded}
      <DropdownInput
        label="Senzor"
        name="sensorId"
        value={sensorDropdownOptions[0][0]}
        options={sensorDropdownOptions}
      />
    {/if}

    {#each Object.keys(currentTemplate) as key}

      {#if currentTemplate[key] === "NUMERIC"}
        <!--TODO change label to translation -->
        <Input label={key} name={"dynamic_" + key} type="number" value=""/>
      {:else if currentTemplate[key] === "TEXT"}
        <!--TODO change label to translation -->
        <Input label={key} name={"dynamic_" + key} type="text" value=""/>
      {:else if currentTemplate[key] === "BOOLEAN"}
        <!--TODO change label to translation -->
        <DropdownInput
          label={key}
          name={"dynamic_" + key}
          value={dataOptions[0][0]}
          options={[
            { value: true, label: "True" },
            { value: false, label: "False" },
          ]}
        />
      {/if}
    {/each}
    <!-- value of the action -->
    <!--    {#if paramsNeeded}-->
    <!--    {/if}-->
    <!--{#if currentTemplate}-->
    <!--  {JSON.stringify(currentTemplate)}-->
    <!--{/if}-->
  </form>

  <div slot="footer">
    <Button
      slot="footer"
      type="confirm"
      autofocus
      formId="addNewAction-{formId}"
      clickType="submit"
      text="Urob to!"
    ></Button>
  </div>
</Modal>
