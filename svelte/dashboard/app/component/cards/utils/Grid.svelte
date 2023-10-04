<script context="module">
    import {getContext} from "svelte";

    const GRID_CONTEXT_NAME = Symbol("grid-context");

    export function getGridContext() {
        let context = getContext(GRID_CONTEXT_NAME);
        if (!context) {
            throw new Error(
                `<GridItem /> is missing a parent <Grid /> component. Make sure you are using the component inside a <Grid />.`,
            );
        }
        return context;
    }
</script>

<script>
    import {setContext} from "svelte";
    import {onMount} from "svelte";
    import GridItem from "./GridItem.svelte";
    import {Item} from "./item";
    import Shadow from "./Shadow.svelte";
    import {Grid} from "./grid";

    export let className = "";
    export let xCount = 1;
    export let yCount = 1;

    export let draggable = false;

    export let padding = 100;

    let rootElement;

    let width = 0;
    let height = 0;

    let resizeObserver;

    let grid = new Grid(width, height, xCount, yCount);

    $: {
        draggable;
        grid.updateItemDraggability(draggable);
    }

    let itemWidth = width / xCount;
    const itemWidthFunctions = {
        getItemWidth() {
            return itemWidth;
        },
        setWidth(value) {
            itemWidth = value;
        },
        updateWidth() {
            itemWidth = (width - padding * Math.max(0, xCount - 1)) / xCount;
        }
    };

    let shadowItemRef = null;
    let shadowItem = null;
    const utilityFunctions = {
        getRootElementRef() {
            return rootElement;
        },
        itemWidth: {...itemWidthFunctions},
        subscribeItem(gridItem) {
            grid.gridItemRefs.push(gridItem);
            gridItem.draggable = draggable;
        },
        /**
         * @return {number}
         */
        getPadding() {
            return padding;
        },
        /**
         *
         * @param item {Item}
         * @param mousePosition {Object} relative to inside of the item
         */
        requestNewPosition(item, mousePosition = null) {
            if (mousePosition != null) {
                let collapsed = collapseMousePosition(item, mousePosition)

                let coords = pointAsCoordinates(
                    collapsed.x,
                    collapsed.y
                );

                item.x = coords.x;
                item.y = coords.y;
            }
            positionGridItem(item);
        },
        setShadow(itemRef) {
            shadowItemRef = itemRef;
            shadowItem = new Item(itemRef.x, itemRef.y, itemRef.w, itemRef.h);
            this.updateShadow()
        },
        updateShadow(itemRelativeMouseX, itemRelativeMouseY) {
            // if there is new relativeMouse value that means that we are moving
            if (itemRelativeMouseX != null && itemRelativeMouseY != null) {
                const collapsed = collapseMousePosition(shadowItemRef, {x: itemRelativeMouseX, y: itemRelativeMouseY})
                let coords = pointAsCoordinates(collapsed.x, collapsed.y);
                shadowItem.x = coords.x;
                shadowItem.y = coords.y;
            }

            //otherwise we just update size by the reference item

            shadowItem.w = Grid.pixelSizeToUnitLength(shadowItemRef.pixelWidth, padding, shadowItemRef.unitSize)
            shadowItem.h = Grid.pixelSizeToUnitLength(shadowItemRef.pixelHeight, padding, shadowItemRef.unitSize)

            // then we position item at the right place 
            positionGridItem(shadowItem);
        },
        getShadowPos() {
            return {x: shadowItem.x, y: shadowItem.y};
        },
        getShadowItem() {
            return shadowItem;
        },
        resetShadow() {
            shadowItem = null;
            shadowItemRef = null;
        },
    };

    setContext(GRID_CONTEXT_NAME, utilityFunctions);

    onMount(() => {
        resizeObserver = new ResizeObserver((entries) => {
            const rect = entries[0].contentRect;
            width = rect.width;
            height = rect.height;
            itemWidthFunctions.updateWidth();

            grid.gridItemRefs.forEach((/** @type {Item} */ gridItem) => {
                positionGridItem(gridItem);
                gridItem.mounted = true;

            });
        });

        resizeObserver.observe(rootElement);

        // Cleanup observer on component destroy
        return () => {
            resizeObserver.disconnect();
        };
    });

    /**
     * Functions for positioning grid item might be a separate file in the future
     * Keep interpolation at minimum
     */

    /**
     *
     * @param gridItem {Item}
     */
    function positionGridItem(gridItem) {
        gridItem.unitSize = itemWidthFunctions.getItemWidth();
        gridItem.xCoordinate =
            itemWidthFunctions.getItemWidth() * gridItem.x +
            padding * Math.max(0, gridItem.x);

        gridItem.yCoordinate =
            itemWidthFunctions.getItemWidth() * gridItem.y +
            padding * Math.max(0, gridItem.y);

    }

    function pointAsCoordinates(x, y) {
        let xCoord = Math.floor(x / (width / xCount));
        let yCoord = Math.floor(y / (height / yCount));
        return {x: xCoord, y: yCoord};
    }

    /**
     * Method 'collapses' relative mouse position so that mouse seems always in first square in itemgrid
     * @param item {Item}
     * @param {Object} mousePosition - The mouse position.
     * @param {number} mousePosition.x - The x-coordinate of the mouse position.
     * @param {number} mousePosition.y - The y-coordinate of the mouse position.
     *
     */
    function collapseMousePosition(item, mousePosition) {
        let xCoord;
        if (mousePosition.x < (item.unitSize + padding / 2)) {
            xCoord = item.xCoordinate + mousePosition.x
        } else {
            xCoord = item.xCoordinate + mousePosition.x - (item.w - 1) * item.unitSize;
        }

        let yCoord;
        if (mousePosition.y < (item.unitSize + padding / 2)) {
            yCoord = item.yCoordinate + mousePosition.y
        } else {
            yCoord = item.yCoordinate + mousePosition.y - (item.h - 1) * item.unitSize;
        }

        return {x: xCoord, y: yCoord}
    }

</script>

<div bind:this={rootElement} class="relative {className}">
    {#if shadowItem}
        <Shadow
                xCoordinate={shadowItem.xCoordinate}
                yCoordinate={shadowItem.yCoordinate}
                pixelWidth={shadowItem.pixelWidth}
                pixelHeight={shadowItem.pixelHeight}
                className="bg-secondary-400"
        ></Shadow>
    {/if}
    <slot/>
</div>
