<script>
    import {getGridContext} from "./Grid.svelte";
    import {Item} from "./item";
    import Button from "../../../../../components/Buttons/Button.svelte";
    import {Dragger} from "./dragger";
    import {onMount} from "svelte";
    import {Resizer} from "./resizer";
    import {Grid} from "./grid";

    export let x = 1;
    export let y = 1;
    export let w = 1;
    export let h = 1;

    export let className = "";


    let gridRoot = getGridContext();

    let item = new Item(x, y, w, h, gridRoot.getPadding());


    let gridItemRoot;
    let resizerElement;

    item.setValueChangedCallback(() => {
        item = item;
    });

    gridRoot.subscribeItem(item);

    onMount(() => {
        let dragger = new Dragger(gridItemRoot, item, gridRoot.getRootElementRef());

        let resizer = new Resizer(resizerElement, item, gridItemRoot, gridRoot);


        item.subscribeMounted(() => {
            dragger.setDraggable(item.draggable);


            item.setDraggableChangedEvent((draggable) => {
                dragger.setDraggable(draggable);
                resizer.setResizable(draggable);
            });

            const initialX = item.xCoordinate;
            const initialY = item.yCoordinate;

            dragger.setOnMouseDownEvent(() => {
                gridRoot.setShadow(item);
            });

            dragger.setOnMouseMoveEvent((x, y) => {
                gridRoot.updateShadow(x, y);
            });

            dragger.setOnMouseUpEvent((event) => {
                gridRoot.resetShadow();

                gridRoot.requestNewPosition(
                    item,
                    dragger.getMouseToItemPosition(event),
                );

                // Reset the item's coordinates to the initial values
                // item.xCoordinate = initialX;
                // item.yCoordinate = initialY;
            });


            resizer.setResizable(item.draggable);

            resizer.setOnMouseUpEvent((event) => {

                item.wh = {
                    w: Grid.pixelSizeToUnitLength(item.pixelWidth, gridRoot.getPadding(), item.unitSize),
                    h: Grid.pixelSizeToUnitLength(item.pixelHeight, gridRoot.getPadding(), item.unitSize)
                }
            })

        });


    });
</script>

<!--outline outline-primary-100 -->
<div
        bind:this={gridItemRoot}
        class="absolute {className}"
        style:height="{item.pixelHeight}px"
        style:width="{item.pixelWidth}px"
        style:top="{item.yCoordinate}px"
        style:left="{item.xCoordinate}px"
>
    <slot/>
    <p>x: {item.x}</p>
    <p>y: {item.y}</p>
    <p>w: {item.w}</p>
    <p>h: {item.h}</p>
    <p>pixelHeight {item.pixelHeight}</p>
    <p>pixelWidth: {item.pixelWidth}</p>

    <p>xPos {item.xCoordinate}</p>
    <p>yPos: {item.yCoordinate}</p>

    <div
            bind:this={resizerElement}
            class="absolute bottom-2 right-2 z-10 h-4 w-4 bg-secondary-400"
    ></div>
</div>
