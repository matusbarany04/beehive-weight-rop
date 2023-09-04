<script>
  import {onMount, setContext} from "svelte";

  import EditPanel from "../../../components/dashboard/panel/EditPanel.svelte";
  
  import Button from "../../../components/Buttons/Button.svelte";
  import {generateUUID} from "../../../components/lib/utils/static";
  import {dataHandler} from "../../../components/cards/dataHandler";
  import * as cardUtils from "../../../components/cards/cardUtils";

  //$: ({ user, sessionid } = $page.data);

  const finalItemCount = 4;

  let itemSideSize = 200;
  let varItemCount = finalItemCount;
  let gridGap = 10;
  let itemsActive = [];
  for (let n = 0; n < varItemCount * varItemCount; n++) {
    itemsActive[n] = false;
  }
  let clientX = 0;
  let clientY = 0;
  let editMode = false; // pridat edit mode premennu do storu nech pretrva aj refreshom
  let smallScreen = false;
  let editButton = true;

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
    return { exists: false, x: -1, y: -1 };
  };

  onMount(function () {
    // message.set(`Dobré ráno, včelár ${user.name}!`);

    initCardList();
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
            { passive: true },
    );
  });

  // true means collision false means no collision
  let collidesWith = (item1, item2) => {
    if (
            item1.x <= item2.x + (item2.spanX - 1) &&
            item1.x + (item1.spanX - 1) >= item2.x &&
            item1.y <= item2.y + (item2.spanY - 1) &&
            item1.spanY - 1 + item1.y >= item2.y
    ) {
      return true;
    } else {
      return false;
    }
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
      const index = cardList
              .map(function (e) {
                return e.id;
              })
              .indexOf(cardID);

      if (index > -1) {
        cardList.splice(index, 1);
      }

      cardList = [...cardList];
    },
    mouseAsCordinates: () => {
      return getPosOfGridItem(clientX, clientY);
    },
    updateCardStates: (cardID, cardStates) => {
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
      console.log("poss", pos, item);
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
      console.log("copy poss", copy.x, copy.y);

      if (!checkCollision(copy)) {
        // item.x = copy.x;
        // item.y = copy.y;
        return { x: copy.x, y: copy.y };
      } else {
        return { x: item.x, y: item.y };
      }
    },
    saveCardList: async () => { // TODO TODO TODO TODO TODO TODO 
      // //TODO check if valid ?
      // // await db.saveDashBoard(cardList, sessionid);
      // const output = await POST(
      //         "",
      //         JSON.stringify({ data: cardList, token: sessionid }),
      // );
      // console.log("saving... ", output);
      // invalidateAll();
    },
  };

  setContext("dashboardEditor", dashboardEditor);

  let cardList = [];
  
  
  const initCardList = () => {
    dataHandler.fetchData().then((output)=>{
      cardList = output;
    })
  };

  const resetCardList = () => {
    cardList = [];
    initCardList();
  };
</script>

<svelte:head>
  <title>DashBoard</title>
  <meta name="description" content="Svelte demo app" />
</svelte:head>

{#if editMode}
  <div class="absolute top-0 left-0">
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
          data: [{ type: "dummy" }],

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

<div class="absolute right-0 top-0 w-full flex justify-end gap-3 p-4 z-50">
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
        class="flex min-h-screen flex-1 justify-center items-center relative w-full"
>
  {#if editMode}
    <div
            class="box-border text-slate-900 w-full h-full absolute grid"
            style:--side="{itemSideSize}px"
            style:--grid-gap="{gridGap}px"
            style:--itemCount={varItemCount}
    >
      {#each { length: Math.pow(varItemCount, 2) } as _, i}
        <div class={"cell gridItem " + (itemsActive[i] ? "active" : "")} />
      {/each}
    </div>
  {/if}

  <div
          class="w-full h-full relative flex-1 grid"
          id="rightPanel"
          style:--itemCount={finalItemCount}
          style:--side="{itemSideSize}px"
          style:--grid-gap="{gridGap}px"
  >
    {#each cardList as item, i}
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
  </div>
</div>


<style lang="scss">
  

  .grid {
    display: grid;
    grid-template-columns: repeat(var(--itemCount), var(--side));
    grid-template-rows: repeat(var(--itemCount), var(--side));
    /* transition: all 100ms linear; */
    align-items: center;
    justify-content: center;
    grid-gap: var(--grid-gap);
    box-sizing: border-box;

    /* grid je posunuty trocha do lava ale som lenivy (pridat gap) */
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
    background-color: hsl(213, 13%, 86%); /* TODO zmenit farbu na nieco pekne */
    width: 100%;
    height: 100%;
    transition: background-color 100ms linear;
  }
  .active {
    background-color: hsl(210, 5%, 71%);
  }

  .item {
    /* outline: 1px black solid ; */
  }
</style>
