<script>
  /**
   * @fileoverview This page shows graphs and charts related to beehives and their data
   * @module HomePage
   */
  import {onMount, setContext, tick} from "svelte";

  import Button from "../../../components/Buttons/Button.svelte";
  import EditPanel from "../component/panel/EditPanel.svelte";
  import * as cardUtils from "../component/cards/cardUtilities";
  import {generateUUID} from "../../../components/lib/utils/staticFuncs";
  import shared, {onLoad} from "../stores/shared";
  import Loading from "../../../components/pages/Loading.svelte";
  import message from "../stores/message";

  const finalItemCount = 4;

  let itemSideSize = 200;
  let varItemCount = finalItemCount;
  let gridGap = 10;
  let itemsActive = [];
  let cardList = [];
  for (let n = 0; n < varItemCount * varItemCount; n++) {
    itemsActive[n] = false;
  }
  let clientX = 0;
  let clientY = 0;
  let editMode = false; // pridat edit mode premennu do storu nech pretrva aj refreshom
  let smallScreen = false;
  let editButton = true;
  let renderCards = false;


  const initCardList = (user) => {
    if (user["dashboardData"]) {
      cardList = JSON.parse(user["dashboardData"]);
    }
  };

  const resetCardList = () => {
    cardList = [];
    initCardList();
  };

  const getPosOfGridItem = (x, y) => {
    let items = document.getElementsByClassName("gridItem");

    for (let i = 0; i < items.length; i++) {
      let element = items[i];

      let itemX = window.scrollX + element.getBoundingClientRect().left;

      let itemY = window.scrollY + element.getBoundingClientRect().top; // Y

      if (
        x > itemX &&
        x < itemX + itemSideSize &&
        y > itemY &&
        y < itemY + itemSideSize
      ) {
        return {
          exists: true,
          x: (i % varItemCount) + 1,
          y: Math.floor(i / varItemCount + 1),
        };
      } else {
        itemsActive[i] = false;
      }
    }
    return {exists: false, x: -1, y: -1};
  };


  message.setMessage("Dobrý deň")
  onLoad(["user"], (user) => {
    message.setMessage("Dobrý deň, včelár " + user.name)
    initCardList(user)
  });

  onMount(function () {
    // message.set(`Dobré ráno, včelár ${user.name}!`);

    onLoad(["beehives", "statuses"], () => {
      let dashboardRoot = document.getElementById("rightPanel");

      const updateItemSideSize = () => {
        varItemCount = finalItemCount;
        smallScreen = false;
        editButton = true;
        itemSideSize =
          (dashboardRoot.getBoundingClientRect().width -
            gridGap * (varItemCount - 1)) /
          varItemCount;

        if (
          (window.innerWidth ||
            document.documentElement.clientWidth ||
            document.body.clientWidth) < 800
        ) {
          varItemCount = 1;
          smallScreen = true;
        }

        if (
          (window.innerWidth ||
            document.documentElement.clientWidth ||
            document.body.clientWidth) < 1200
        ) {
          editMode = false;
          editButton = false;
        }
      };

      /* tuto funkciu som pridal lebo bocny panel sa zmensi a karticky sa neupdatnu*/
      const asyncLoop = () => {
        // drz to na pamati mozno to bude zrat pamat
        setTimeout(function () {
          updateItemSideSize();
          asyncLoop();
        }, 250);
      };
      asyncLoop();

      updateItemSideSize();
      window.addEventListener(
        "resize",
        function (event) {
          updateItemSideSize();
        },
        true,
      );

      document.addEventListener(
        "mousemove",
        (e) => {
          clientX = e.clientX;
          clientY = e.clientY;
          let pos = getPosOfGridItem(e.clientX, e.clientY);

          for (let n = 0; n < finalItemCount * finalItemCount; n++) {
            itemsActive[n] = false;
          }
          if (pos.exists) {
            itemsActive[(pos.y - 1) * varItemCount + pos.x - 1] = true;
          }
          itemsActive = [...itemsActive];
        },
        {passive: true},
      );
    });
  });

  // true means collision false means no collision
  export let collidesWith = (item1, item2) => {
    return (
      item1.x <= item2.x + (item2.spanX - 1) &&
      item1.x + (item1.spanX - 1) >= item2.x &&
      item1.y <= item2.y + (item2.spanY - 1) &&
      item1.spanY - 1 + item1.y >= item2.y
    );
  };

  let checkCollision = (node) => {
    for (let i = 0; i < cardList.length; i++) {
      let element = cardList[i];

      if (element.id !== node.id) {
        if (collidesWith(node, element)) {
          return true;
        }
      }
    }
    return false;
  };

  const dashboardEditor = {
    deleteCard: (cardID) => {
      cardList = cardList.filter((card) => card.id !== cardID);
      cardList = [...cardList];
    },
    mouseAsCordinates: () => {
      return getPosOfGridItem(clientX, clientY);
    },
    updateCardStates: function (cardID, cardStates) {
      //TODO pridat check validity cardStatus
      for (let i = 0; i < cardList.length; i++) {
        if (cardList[i].id === cardID) {
          cardList[i].x = cardStates.x;
          cardList[i].y = cardStates.y;
          cardList[i].spanX = cardStates.spanX;
          cardList[i].spanY = cardStates.spanY;
          cardList[i].title = cardStates.title;
          cardList[i].data = cardStates.data;
        }
      }
      cardList = [...cardList];
    },
    cordinatesAsPosition: (x, y) => {
      return {
        x: itemSideSize * (x - 1) + gridGap * (x - 1),
        y: itemSideSize * (y - 1) + gridGap * (y - 1),
      };
    },
    getItemMargin: () => {
      return gridGap;
    },
    updateCardStatesPos: (x, y, cardID) => {
      var index = cardList
        .map(function (e) {
          return e.id;
        })
        .indexOf(cardID);

      if (index > -1) {
        cardList[index].x = x;
        cardList[index].y = y;
      }
    },
    getItemSideSize: () => {
      return itemSideSize;
    },
    figureEndPosition: (marX, marY, item) => {
      // marX  a marY su relativne pozicie karty na ktorej je myska
      let pos = getPosOfGridItem(clientX, clientY);
      // console.log("poss", pos, item);
      let copy = JSON.parse(JSON.stringify(item));
      /* pos.exists je len pre 1x1 kartu */
      if (pos.exists) {
        // to avoid out of bounds issues
        if (pos.x - 1 - marX + item.spanX > varItemCount) {
          //doprava
          copy.x = varItemCount - item.spanX + 1;
        } else if (pos.x - 1 - marX < 0) {
          //dolava
          copy.x = 1;
        } else {
          copy.x = pos.x - marX;
        }

        if (pos.y - 1 - marY + item.spanY > varItemCount) {
          //doprava
          copy.y = varItemCount - item.spanY + 1;
        } else if (pos.y - 1 - marY < 0) {
          //dolava
          copy.y = 1;
        } else {
          copy.y = pos.y - marY;
        }
      }
      // console.log("copy poss", copy.x, copy.y);

      if (!checkCollision(copy)) {
        // item.x = copy.x;
        // item.y = copy.y;
        return {x: copy.x, y: copy.y};
      } else {
        return {x: item.x, y: item.y};
      }
    },
    saveCardList: async () => {
      //TODO check if valid ?
      // await db.saveDashBoard(cardList, sessionid);
      try {
        const response = await fetch("/user/saveDashboard", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({data: JSON.stringify(cardList)}), // TOKEN needed , token: sessionid
        });
        const output = await response.json();
        console.log("saving... ", output);

        // BUG TODO invalidateAll();
      } catch (error) {
        console.error("Error saving card list:", error);
      }
    },
  };

  setContext("dashboardEditor", dashboardEditor);

  onLoad(["beehives", "statuses"], () => {
    renderCards = true;
  });
</script>

<svelte:head>
  <title>DashBoard</title>
  <meta name="description" content="Svelte demo app"/>
</svelte:head>

{#if editMode}
  <div class="absolute left-0 top-0">
    <EditPanel
      onDragStart={(x, y, self) => {
        let pos = getPosOfGridItem(clientX, clientY);
        return { marX: 1, marY: 1, cardSize: itemSideSize };
      }}
      onDragEnd={(endX, endY, self) => {
        let pos = { x: endX, y: endY }; //getPosOfGridItem(clientX, clientY);
        console.log("edit panel pos", pos);

        const potentialCard = {
          id: generateUUID(),
          x: pos.x,
          y: pos.y,
          spanX: 1,
          spanY: 1,
          component: self.component,
          title: self.format, // TODO zmenit na nejaky string z listu, aby bola mozna localizacia
          data: [{ type: "dummy", from: "all", to: "now" }], //default type of data in card item
        };

        if (
          pos.x > 0 &&
          pos.y > 0 &&
          pos.x <= finalItemCount &&
          pos.y <= finalItemCount
        ) {
          //TODO kolizia check (je na to funckia len som lenivy)
          if (!checkCollision(potentialCard)) {
            cardList.push(potentialCard);
            cardList = [...cardList]; // pre updatovanie ui-ka
          }
        }
        return { marX: pos.x, marY: pos.y };
      }}
    />
  </div>
{/if}

<div class="absolute right-0 top-0 z-50 flex w-min justify-end gap-3 p-4">
  {#if editButton}
    <div class="flex gap-4">
      {#if editMode}
        <Button
          text="Zahodiť zmeny"
          type="secondary"
          onClick={() => {
            editMode = !editMode;
            resetCardList();
          }}
        />
      {/if}
      <!-- TODO spravit len disabled mozno v buducnosti, 
                            pridat aj popup preco to zmizlo (ked sa zmensi sirka okna) -->
      <Button
        text={!editMode ? "upraviť" : "uložiť"}
        type={!editMode ? "primary" : "confirm"}
        onClick={() => {
          if (editMode) {
            dashboardEditor.saveCardList();
          }
          editMode = !editMode;
        }}
      />
    </div>
  {/if}
</div>

<div
  class="relative flex min-h-screen w-full flex-1 items-center justify-center"
>
  {#if editMode}
    <div
      class="absolute box-border grid h-full w-full text-slate-900"
      style:--side="{itemSideSize}px"
      style:--grid-gap="{gridGap}px"
      style:--itemCount={varItemCount}
    >
      {#each {length: Math.pow(varItemCount, 2)} as _, i}
        <div class={"cell gridItem " + (itemsActive[i] ? "active" : "")}/>
      {/each}
    </div>
  {/if}

  <div
    class="relative h-full w-full flex-1 {renderCards ? 'grid' : ''}"
    id="rightPanel"
    style:--itemCount={finalItemCount}
    style:--side="{itemSideSize}px"
    style:--grid-gap="{gridGap}px"
  >
    {#if renderCards}
      {#each cardList as item, i (item.id)}
        <svelte:component
          this={cardUtils.getCardByFormat(item.component)}
          cardStates={{
            id: item.id,
            x: smallScreen ? 1 : item.x,
            y: smallScreen ? i + 1 : item.y,
            spanX: smallScreen ? 1 : item.spanX,
            spanY: smallScreen ? 1 : item.spanY,
            editing: editMode,
            title: item.title,
            data: item.data ?? [],
          }}
          onDragStart={(x, y, self) => {
            let itemWidth =
              itemSideSize * item.spanX + gridGap * (item.spanX - 1);
            let itemHeight =
              itemSideSize * item.spanY + gridGap * (item.spanY - 1);

            let leftMar = clientX - x;
            let topMar = clientY - y;

            let mouseXIndex = Math.floor(leftMar / (itemWidth / item.spanX));
            let mouseYIndex = Math.floor(topMar / (itemHeight / item.spanY));

            return { marX: mouseXIndex, marY: mouseYIndex };
          }}
          onDragEnd={(endX, endY, self) => {
            dashboardEditor.updateCardStatesPos(endX, endY, self.id);
          }}
        />
      {/each}
    {:else}
      <Loading/>
    {/if}
  </div>
</div>

<style lang="scss">
  .grid {
    display: grid;
    grid-template-columns: repeat(var(--itemCount), var(--side));
    grid-template-rows: repeat(var(--itemCount), var(--side));
    align-items: center;
    justify-content: center;
    grid-gap: var(--grid-gap);
    box-sizing: border-box;

    @media (max-width: 800px) {
      grid-template-columns: repeat(1, calc(var(--side) * var(--itemCount)));
      grid-template-rows: repeat(
        calc(var(--itemCount) * var(--itemCount)),
          calc(var(--side) * var(--itemCount))
      );
    }
  }

  .gridItem {
    width: 100%;
    aspect-ratio: 1/1;
  }

  .cell {
    background-color: hsl(213, 13%, 86%);
    width: 100%;
    height: 100%;
    transition: background-color 100ms linear;
  }

  .active {
    background-color: hsl(210, 5%, 71%);
  }
</style>
