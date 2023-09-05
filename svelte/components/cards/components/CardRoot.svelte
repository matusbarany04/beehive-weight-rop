<script>
  import { onMount, getContext } from "svelte";
  import { modes, getDefaultMode } from "./cardConfig";

  import { spring } from "svelte/motion";
  import CardError from "../errors/CardError.svelte";
  import Modal from "../../Modal.svelte";
  import Input from "../../Inputs/Input.svelte";
  import Button from "../../Buttons/Button.svelte";
  import {
    isEmpty,
  } from "../../lib/utils/static";
  // import { applyAction } from "$app/forms";

  //TODO pridat check funkciu ci su tu vsetky premenne
  /**
   * @param {Object} cardStates the card states of the card
   */
  export let cardStates = {
    id: null,
    x: 1,
    y: 1,
    spanX: 1,
    spanY: 1,
    title: null,
    mode: getDefaultMode(),
    editing: false,
    data: [],
  };

  /**
   * @param {function} updateSettings function to update Settings of the card, only used in a dashboard view
   */
  export let updateSettings;
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
  export let onCardStatesModified = () => {};

  const dashboardEditor = getContext("dashboardEditor");
  // dashboardEditor.deleteCard("some dynamic id");

  if (cardStates.mode == null) getDefaultMode();
  // export let editing;
  if (cardStates.editing === true) cardStates.mode = "edit";
  if (cardStates.mode === "add") cardStates.editing = true;

  export let onDragEnd; // function
  export let onDragStart; // function

  let dragDisabled = false;
  let showSettings = false;
  let coords = spring(
    { x: 0, y: 0 },
    {
      stiffness: 0.1,
      damping: 0.25,
    },
  );

  //TODO
  //preklik na detail dashboardy
  // pri chrome davat aj zoom ako premennu

  onMount(function () {});

  function dragMe(node) {
    let moving = false;

    let out;

    if (cardStates.mode === "add") {
      node.addEventListener("mousedown", () => {
        console.log("mouse down");
        if (cardStates.editing && !dragDisabled) {
          let boundingRect = node.getBoundingClientRect();
          out = onDragStart(boundingRect.left, boundingRect.top, selfAsJson());
          moving = true;

          let margins = {
            x: (boundingRect.width - dashboardEditor.getItemSideSize()) / 2,
            y: (boundingRect.height - dashboardEditor.getItemSideSize()) / 2,
          };

          let top = boundingRect.top + margins.y;
          let left = boundingRect.left + margins.x;

          node.style.position = "absolute";
          // node.style.top = `${top}px`;
          // node.style.left = `${left}px`;
          coords.set({ x: left, y: top }, { hard: true });

          node.style.cursor = "move";
          node.style.userSelect = "none";
          node.style.zIndex = "500";

          node.style.width = `${dashboardEditor.getItemSideSize()}px`;
          node.style.height = `${dashboardEditor.getItemSideSize()}px`;
        }
      });

      node.addEventListener("mousemove", (e) => {
        if (moving && cardStates.editing && !dragDisabled) {
          // left += e.movementX;
          // top += e.movementY;
          // node.style.top = `${top}px`;
          // node.style.left = `${left}px`;

          coords.set(
            {
              x: $coords.x + e.movementX,
              y: $coords.y + e.movementY,
            },
            { hard: true },
          );
        }
      });

      node.addEventListener("mouseup", () => {
        if (moving && cardStates.editing && !dragDisabled) {
          moving = false;
          node.style.position = "static";
          node.style.zIndex = "2";
          node.style.cursor = "default";
          let boundingRect = node.getBoundingClientRect();

          if (cardStates.mode === "add") {
            node.style.width = `auto`;
            node.style.height = `auto`;
          }
          let { x: endX, y: endY } = dashboardEditor.mouseAsCordinates();

          onDragEnd(endX, endY, selfAsJson());
        }
      });
    } else {
      node.addEventListener("mousemove", (e) => {
        console.log("edit mouse move");
        if (moving && cardStates.editing && !dragDisabled) {
          coords.set(
            {
              x: $coords.x + e.movementX,
              y: $coords.y + e.movementY,
            },
            { hard: true },
          );
        }
      });
      node.addEventListener("mousedown", () => {
        console.log("edit mouse down");
        //TODO spravit aby ked clovek klikne na tlacidla tak sa velkost karty nezmeni
        if (cardStates.editing && !dragDisabled) {
          let boundingRect = node.getBoundingClientRect();
          out = onDragStart(boundingRect.left, boundingRect.top, selfAsJson());
          moving = true;
          node.style.position = "absolute";
          coords.set({ x: out.marX, y: out.marY }, { hard: true });
          node.style.cursor = "move";
          node.style.userSelect = "none";
          node.style.zIndex = "9999";

          if (cardStates.mode === "add") {
            node.style.width = `${dashboardEditor.getItemSideSize()}px`;
            node.style.height = `${dashboardEditor.getItemSideSize()}px`;
          }
        }
      });

      window.addEventListener("mouseup", () => {
        console.log("edit mouse up");
        if (moving && cardStates.editing && !dragDisabled) {
          moving = false;

          let { x: endX, y: endY } = dashboardEditor.figureEndPosition(
            out.marX,
            out.marY,
            selfAsJson(),
          );
          let endPosition = dashboardEditor.cordinatesAsPosition(endX, endY);

          let startPosition = dashboardEditor.cordinatesAsPosition(
            cardStates.x,
            cardStates.y,
          );

          dragDisabled = true;
          coords
            .set(
              {
                x: endPosition.x - startPosition.x,
                y: endPosition.y - startPosition.y,
              },
              { soft: 0.1 },
            )
            .then(() => {
              dragDisabled = false;
              node.style.position = "static";
              node.style.zIndex = "2";
              node.style.cursor = "default";
              onDragEnd(endX, endY, selfAsJson()); //out.marX, out.marY
              if (cardStates.mode === "add") {
                node.style.width = `auto`;
                node.style.height = `auto`;
              }
            });

          // left = 0;
          // top = 0;
        }
      });
    }
    //TODO uncomment
    // window.addEventListener("mouseout", (e) => {
    //     var from = e.relatedTarget || e.toElement;
    //     if (!from || from.nodeName == "HTML") {
    //         // console.log("the mouse left the window");

    //         if (moving && !dragDisabled) {
    //             moving = false;

    //             coords
    //                 .set(
    //                     {
    //                         x: out.marX,
    //                         y: out.marY,
    //                     },
    //                     { soft: 0.1 }
    //                 )
    //                 .then(() => {
    //                     dragDisabled = false;
    //                     node.style.position = "static";
    //                     node.style.zIndex = "2";
    //                     node.style.cursor = "default";

    //                     if (cardStates.mode == "add") {
    //                         node.style.width = `auto`;
    //                         node.style.height = `auto`;
    //                     }
    //                 });
    //         }
    //     }
    // });
  }

  function handleResize(handle, vector) {
    if (cardStates.mode === "add") {
      return 1;
    }
    let out;
    let handlingHandle = false;
    let pos = { x: cardStates.x, y: cardStates.y };
    let spans = { x: cardStates.spanX, y: cardStates.spanY };
    // let handle = document.getElementsByClassName("handle")[0];
    handle.addEventListener("mousedown", () => {
      if (cardStates.editing) {
        dragDisabled = true;
        handlingHandle = true;
        pos = { x: cardStates.x, y: cardStates.y };
        spans = { x: cardStates.spanX, y: cardStates.spanY };
      }
    });

    document.addEventListener("mousemove", (e) => {
      //TODO mozno sa bude dat optimalizovat

      // onCardStatesModified
      if (cardStates.editing && dragDisabled && handlingHandle) {
        let newPos = dashboardEditor.mouseAsCordinates();

        if (newPos.exists) {
          if (cardStates.x !== newPos.x || cardStates.y !== newPos.y) {
            if (vector.y === -1) {
              cardStates.y = Math.min(pos.y + spans.y - 1, newPos.y);
              cardStates.spanY = pos.y - newPos.y + spans.y;
            }

            if (vector.x === -1) {
              cardStates.x = Math.min(pos.x + spans.x - 1, newPos.x);
              cardStates.spanX = pos.x - newPos.x + spans.x;
            }

            onCardStatesModified();
          }

          let newSpanX = Math.max(0, newPos.x - cardStates.x) + 1;
          let newSpanY = Math.max(0, newPos.y - cardStates.y) + 1;

          if (cardStates.spanX !== newSpanX) {
            if (vector.x === 1) {
              onCardStatesModified();
              cardStates.spanX = newSpanX;
            }
          }
          if (cardStates.spanY !== newSpanY) {
            if (vector.y === 1) {
              onCardStatesModified();
              cardStates.spanY = newSpanY;
            }
          }
        }
      }
    });

    document.addEventListener("mouseup", () => {
      console.log("handle  mouse up");
      if (handlingHandle || dragDisabled) {
        dragDisabled = false;
        handlingHandle = false;
        dashboardEditor.updateCardStates(cardStates.id, cardStates);
      }
    });

    // handle.addEventListener("mouseleave", () => {
    //     dragDisabled = false
    // });
  }

  const selfAsJson = () => {
    let json = JSON.parse(JSON.stringify(cardStates));
    json.component = component;
    return json;
  };

  let form;
  async function handleSubmit(event) {
    const data = new FormData(this);
    form = {};

    let title = data.get("title");
    if (!isEmpty(title)) {
      console.log("settings new title", title);
      cardStates.title = title;
    }
    // let data_type = data.get("data_type");

    let output = updateSettings(data);
    if (output.status === "success") {
      cardStates.data = output.data;
      console.log("cardState s", cardStates);
    } else {
      form.error = "Chyba";
    }

    dashboardEditor.updateCardStates(cardStates.id, cardStates);
    dashboardEditor.saveCardList();

    showSettings = false;
    
    // invalidateAll(); //TODO  might come back as bug
    //might also come as a bug
    // applyAction({ type: "success" });
  }
</script>

<div
  use:dragMe
  class={"root overflow-hidden flex justify-center align-middle rounded-lg w-full h-full aspect-square " +
    "root_" +
    theme}
  style:left={$coords.x + "px"}
  style:top={$coords.y + "px"}
  style:--x={cardStates.x}
  style:--y={cardStates.y}
  style:--sx={cardStates.spanX}
  style:--sy={cardStates.spanY}
>
  <div class="relative h-full flex flex-col w-full">
    <div class="w-full h-8 flex px-3 py-1 box-border z-50 my-1">
      <h1
        class="flex-1 text-base text-slate-500 text-ellipsis no_wrap whitespace-nowrap"
      >
        {cardStates?.title || ""}
      </h1>
      <slot name="header" />

      {#if cardStates.mode !== "add" && cardStates.mode !== "static"}
        <!-- svelte-ignore a11y-click-events-have-key-events -->
        <button
          class="headerIcon options"
          on:click={() => {
            showSettings = true;
          }}
        ></button>
      {/if}
      {#if cardStates.editing && cardStates.mode !== "add"}
        <!-- svelte-ignore a11y-click-events-have-key-events -->
        <div
          class="headerIcon removeImage"
          on:click={() => {
            dashboardEditor.deleteCard(cardStates.id);
          }}
        />
      {/if}
      {#if cardStates.mode === "add"}
        <div class="headerIcon addBtn" />
      {/if}
    </div>
    {#if cardStates.editing && cardStates.mode !== "add"}
      <div class="editHandles">
        <div class="handleCont">
          <div class="handle right" use:handleResize={{ x: 1, y: 0 }} />
          <div class="handle left" use:handleResize={{ x: -1, y: 0 }} />
          <div class="handle top" use:handleResize={{ x: 0, y: -1 }} />
          <div class="handle bottom" use:handleResize={{ x: 0, y: 1 }} />
        </div>
      </div>
    {/if}

    <div id="customContent" class="flex w-full h-[calc(100%-2rem)]">
      {#if !error}
        <slot />
      {:else}
        <CardError {error} />
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
    id="cardRootForm"
    action="?/saveSettings"
    method="POST"
    class="flex flex-col gap-4 my-4"
  >
    <Input
      label="Názov karty"
      placeholder="Názov"
      name="title"
      value={cardStates?.title ?? ""}
    ></Input>

    <slot name="customSettings" />
  </form>

  <button slot="footer" type="submit" form="cardRootForm">
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
  .handleCont {
    position: relative;
    width: 100%;
    height: 100%;
  }

  .handle {
    z-index: 15;
    background-color: var(--color-primary);
    border-radius: 5px;
    inset: 0;
    margin: auto;
    position: absolute;
    transition-duration: 100ms;
    &:hover {
      transform: scale(1.1);
      cursor: pointer;
    }
  }
  .top {
    width: var(--width);
    height: var(--length);
    margin-top: 0px;
  }
  .bottom {
    width: var(--width);
    height: var(--length);
    margin-bottom: 0;
  }
  .right {
    width: var(--length);
    height: var(--width);
    margin-right: 0;
  }
  .left {
    width: var(--length);
    height: var(--width);
    margin-left: 0;
  }
  
  
  .editHandles {
    position: absolute;
    width: 100%;
    height: 100%;
    --length: 5px;
    --width: 40px;

    
 
   
  }

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
    background: var(--color-delete);
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
    outline: 1px solid #cfd2d6;
    outline-offset: -1px;
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
