<script>
    import {getGridContext} from "./Grid.svelte";
    import {Item} from "./item";
    import Button from "../../../../../components/Buttons/Button.svelte";
    import {Dragger} from "./dragger";
    import {onMount} from "svelte";
    import {Resizer} from "./resizer";
    import {Grid} from "./grid";
    import {spring} from "svelte/motion";

    export let x = 1;
    export let y = 1;
    export let w = 1;
    export let h = 1;

    export let className = "";

    let gridRoot = getGridContext();

    let item = new Item(x, y, w, h, gridRoot.getPadding());
    item.padding = gridRoot.getPadding();

    let gridItemRoot;
    let resizerElement;

    let pixelSize = spring(
        {pixelHeight: item.pixelHeight, pixelWidth: item.pixelWidth},
        {
            stiffness: 0.1,
            damping: 0.25,
        },
    );
    let itemCoords = spring(
        {x: item.xCoordinate, y: item.yCoordinate},
        {
            stiffness: 0.1,
            damping: 0.25,
        },
    );

    item.setValueChangedCallback(() => {
        console.log("value changed")
        item = item;
        itemCoords.set({x: item.xCoordinate, y: item.yCoordinate});
        pixelSize.set({
            pixelHeight: item.pixelHeight,
            pixelWidth: item.pixelWidth,
        });
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
                    w: Grid.pixelSizeToUnitLength(
                        item.pixelWidth,
                        gridRoot.getPadding(),
                        item.unitSize,
                    ),
                    h: Grid.pixelSizeToUnitLength(
                        item.pixelHeight,
                        gridRoot.getPadding(),
                        item.unitSize,
                    ),
                };
            });
        });
    });
</script>

<!--outline outline-primary-100 -->
<div
        bind:this={gridItemRoot}
        class="absolute {className}"
        style:width="{$pixelSize.pixelWidth}px"
        style:height="{$pixelSize.pixelHeight}px"
        style:left="{$itemCoords.x}px"
        style:top="{$itemCoords.y}px"
>
    <slot/>
    <div class="box-border h-full w-full p-4">
        <p>x: {item.x}</p>
        <p>y: {item.y}</p>
        <p>w: {item.w}</p>
        <p>h: {item.h}</p>
        <p>pixelHeight {item.pixelHeight}</p>
        <p>pixelWidth: {item.pixelWidth}</p>
        <p>pixelWidth: {item.draggable}</p>

        <p>xPos {item.xCoordinate}</p>
        <p>yPos: {item.yCoordinate}</p>
    </div>

    <div
            bind:this={resizerElement}
            style:background-image="url('/icons/arrows-fullscreen.svg')"
            class="absolute bottom-1.5 right-1.5 z-10 h-4 w-4 {!item.draggable ? 'hidden' : ''}"
    ></div>

</div>
