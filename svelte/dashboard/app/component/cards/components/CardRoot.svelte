<script>
  import {onMount, getContext} from "svelte";
  import {modes, getDefaultMode} from "./cardConfiguration";

  import {spring} from "svelte/motion";
  import CardError from "../errors/CardError.svelte";
  import Modal from "../../../../../components/Modal.svelte";
  import Input from "../../../../../components/Inputs/Input.svelte";
  import Button from "../../../../../components/Buttons/Button.svelte";

  import {
    generateUUID,
    isEmpty,
  } from "../../../../../components/lib/utils/staticFuncs";

  //TODO pridat check funkciu ci su tu vsetky premenne
  /**
   * @param {Object} cardStates the card states of the card
   */
  export let cardStates = {
    id: null,
    title: null,
    mode: getDefaultMode(),
    editing: false,
    data: [],
  };

  export let className = "";

  /**
   * @param {function} updateSettings function to update Settings of the card, only used in a dashboard view
   */
  export let updateSettings = () => {
    return {status: "success"};
  };
  /**
   * @param {String} theme default one is 'default' other options are: 'dashed'
   */
  export let theme = "default";
  /**
   * @param {String} component name of the card from card config
   */
  export let component;
  /**
   * @param {String} error type of the card from error.js
   */
  export let error;
  /**
   * @param {function} onCardStatesModified used to update states in a dashboard view
   */
  export let onCardStatesModified = () => {
  };

  const dashboard = getContext("dashboard");
  // dashboardEditor.deleteCard("some dynamic id");

  if (!cardStates.mode) getDefaultMode();

  if (cardStates.editing === true) cardStates.mode = "edit";
  if (cardStates.mode === "add") cardStates.editing = true;

  export let onDragEnd; // function
  export let onDragStart; // function

  let dragDisabled = false;
  let showSettings = false;

  let coords = spring(
    {x: 0, y: 0},
    {
      stiffness: 0.1,
      damping: 0.4,
    },
  );

  const selfAsJson = () => {
    let json = JSON.parse(JSON.stringify(cardStates));
    json.component = component;
    return json;
  };

  let form;

  async function handleSubmit(event) {
    // we get form data
    const data = new FormData(this);
    form = {};
    // console.log("saving these data", data);
    let title = data.get("title");

    // every child card has implemented their own handler
    let output = updateSettings(data);

    if (!isEmpty(title)) {
      cardStates.title = title;
    }

    if (output.status === "success") {
      cardStates.data = output.data;
    } else {
      form.error = "Chyba";
    }
    await dashboard.saveGrid();

    showSettings = false;

    // invalidateAll(); //TODO  might come back as bug
    //might also come as a bug
    // applyAction({ type: "success" });
  }

  let formID = generateUUID();
  let cardRootRoot;

  export let resizedEvent = () => {
  };

  onMount(() => {
    const resizeObserver = new ResizeObserver(() => {
      resizedEvent();
    });

    resizeObserver.observe(cardRootRoot);

    // Cleanup function
    return () => {
      resizeObserver.disconnect();
    };
  });

  let gridExport = getContext("gridItem");
  if (gridExport !== undefined) {
    gridExport.setDataExportFunction(() => {
      return {props: {cardStates: cardStates}, component: component};
    });
  }
</script>

<div
  class="flex aspect-square h-full w-full select-none justify-center overflow-hidden rounded-md align-middle {className} {'root_' +
    theme}"
  bind:this={cardRootRoot}
>
  <div class="relative flex h-full w-full flex-col">
    <div class="z-20 my-1 box-border flex h-8 w-full px-3 py-1 pr-7">
      <h1
        class="no_wrap flex-1 text-ellipsis whitespace-nowrap text-base text-slate-500"
      >
        {error || cardStates?.title || ""}
      </h1>
      <slot name="header"/>

      {#if cardStates.mode !== "add" && cardStates.mode !== "static"}
        <!-- svelte-ignore a11y-click-events-have-key-events -->
        <button
          class="headerIcon options"
          on:click={() => {
            showSettings = true;
          }}
        ></button>
      {/if}
      <!--{#if cardStates.editing && cardStates.mode !== "add"}-->
      <!--        <button-->
      <!--          class="headerIcon removeImage"-->
      <!--          on:click={() => {-->
      <!--            dashboardEditor.deleteCard(cardStates.id);-->
      <!--          }}-->
      <!--        />-->
      <!--      {/if}-->
      {#if cardStates.mode === "add"}
        <div class="headerIcon addBtn"/>
      {/if}
    </div>
    <div id="customContent" class="flex h-[calc(100%-2rem)] w-full">

      <slot/>
      {#if error}
        <div class="absolute top-0 left-0 w-full h-full">
          <CardError {error}/>
        </div>
      {/if}
    </div>
  </div>
</div>

<Modal bind:showModal={showSettings}>
  <h2 slot="header" class="text-2xl font-bold">
    {"Upraviť nastavenia karty"}
  </h2>

  <form
    on:submit|preventDefault={handleSubmit}
    id="cardRootForm{formID}"
    method="POST"
    class="my-4 flex flex-col gap-4"
  >
    <Input
      label="Názov karty"
      placeholder="Názov"
      name="title"
      value={cardStates?.title ?? ""}
    ></Input>

    <slot name="customSettings"/>
  </form>

  <code>
    {JSON.stringify(cardStates)}
  </code>

  <button slot="footer" type="submit" form="cardRootForm{formID}">
    <Button
      slot="footer"
      type="confirm"
      autofocus
      onClick={() => {
        // saveSettings();
        // dialog.close();
      }}
      clickType="submit"
      text="Uložiť a zatvoriť okno"
    ></Button>
  </button>
</Modal>

<style lang="scss">
  .headerIcon {
    height: 80%;
    aspect-ratio: 1/1;
    margin-left: 5px;
    z-index: 10;

    &:hover {
      transform: scale(1.05);
      cursor: pointer;
    }
  }

  .options {
    background-image: url("/icons/options.svg");
    background-size: contain;
    background-position: center;
    background-repeat: no-repeat;
  }

  .removeButton {
    cursor: pointer;
  }

  .removeButton:hover {
    transform: scale(1.05);
  }

  .removeButton:active {
    transform: scale(0.98);
  }

  .removeImage {
    width: 20px;
    height: 20px;
    background-size: cover;
    isolation: isolate;
    -webkit-isolation: isolate;
    -webkit-mask-mode: alpha;
    -webkit-mask-size: 100%;
    mask-size: 100%;
    background: red;
    mask-image: url("/icons/delete.svg");
    -webkit-mask-image: url("/icons/delete.svg");
  }

  .addBtn {
    width: 20px;
    height: 20px;
    background-size: cover;
    isolation: isolate;
    -webkit-isolation: isolate;
    -webkit-mask-mode: alpha;
    -webkit-mask-size: 100%;
    mask-size: 100%;
    background: var(--color-accept);
    mask-image: url("/icons/add.svg");
    -webkit-mask-image: url("/icons/add.svg");
  }

  .root_default {
    background-color: rgb(255, 255, 255);
    //outline: 1px solid #cfd2d6;
    //outline-offset: -1px;
  }

  .root_dashed {
    background-color: var(--color-tertiary);
    outline: 4px dashed #888585;
    outline-offset: -4px;
  }

  .root {
    grid-column-start: var(--x);
    grid-row-start: var(--y);
    grid-column-end: calc(var(--x) + var(--sx));
    grid-row-end: calc(var(--y) + var(--sy));
  }
</style>
