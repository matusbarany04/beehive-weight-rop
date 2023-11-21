import { generateUUID } from "../../../../../components/lib/utils/staticFuncs";
import { GridResolver } from "./gridResolver";
import toast from "../../../../../components/Toast/toast";

export class Grid {
  /** @type {string} */
  _id;

  /** @type {number} */
  _itemCount;

  /** @type {number} */
  _widthPixel;

  /** @type {number} */
  _heightPixel;

  /**
   * @type array<Item>
   */
  _gridItemsRefs = [];

  /** @type {number} */
  _xCount;

  /** @type {number} */
  _yCount;

  /**
   *
   * @param {number} [width=0] - Grid width.
   * @param {number} [height=0] - Grid height.
   * @param xCount {number}
   * @param yCount {number}
   */
  constructor(width = 0, height = 0, xCount = 1, yCount = 1) {
    this._id = generateUUID();
    this._itemCount = 0;
    this._widthPixel = width;
    this._heightPixel = height;
    this._xCount = xCount;
    this._yCount = yCount;
    this._gridItemRefs = [];
  }

  /**
   * Getter for id.
   * @return {string}
   */
  get id() {
    return this._id;
  }

  /**
   * Setter for id.
   * @param {string} value
   */
  set id(value) {
    this._id = value;
  }

  /**
   * Getter for itemCount.
   * @return {number}
   */
  get itemCount() {
    return this._itemCount;
  }

  /**
   * Setter for itemCount.
   * @param {number} value
   */
  set itemCount(value) {
    this._itemCount = value;
  }

  /**
   * Getter for width.
   * @return {number}
   */
  get widthPixel() {
    return this._widthPixel;
  }

  /**
   * Setter for width.
   * @param {number} value
   */
  set widthPixel(value) {
    this._widthPixel = value;
  }

  /**
   * Getter for height.
   * @return {number}
   */
  get heightPixel() {
    return this._heightPixel;
  }

  /**
   * Setter for height.
   * @param {number} value
   */
  set heightPixel(value) {
    this._heightPixel = value;
  }

  get xCount() {
    return this._xCount;
  }

  set xCount(value) {
    this._xCount = value;
  }

  get yCount() {
    return this._yCount;
  }

  set yCount(value) {
    this._yCount = value;
  }

  get gridItemRefs() {
    return this._gridItemRefs;
  }

  set gridItemRefs(value) {
    this._gridItemRefs = value;
  }

  pointAsCoordinates(x, y) {
    let xCoord = Math.floor(x / (this._widthPixel / xCount));
    let yCoord = Math.floor(y / (this._heightPixel / yCount));
    return { x: xCoord, y: yCoord };
  }

  /**
   *
   * @param item1 {Item}
   * @param item2 {Item}
   * @return {boolean}
   */
  collidesWith(item1, item2) {
    return (
      item1.x <= item2.x + (item2.w - 1) &&
      item1.x + (item1.w - 1) >= item2.x &&
      item1.y <= item2.y + (item2.h - 1) &&
      item1.h - 1 + item1.y >= item2.y
    );
  }

  /**
   * @param id {string}
   */
  getItemById(id) {
    for (/** @type {Item} */ const item of this._gridItemsRefs) {
      if (item.id === id) return item;
    }
    console.error(`Item with id ${id} doesn't exist!!`);
    return null;
  }

  updateItemDraggability(draggable) {
    for (/** @type {Item} */ const item of this.gridItemRefs) {
      item.draggable = draggable;
    }
  }

  /**
   * Function return the closest unit size * multiplier that pixelSize would occupy
   * @param {number} pixelSize - Absolute value from top or left of the grid.
   * @param {number} padding - Space between elements in grid.
   * @param {number} unitSize - Size of an element.
   * @returns {number|1} - The number of units, lowest one is 1.
   */
  static pixelSizeToUnitLength(pixelSize, padding, unitSize) {
    const totalSizePerUnit = unitSize + padding;
    return Math.max(1, Math.round(pixelSize / totalSizePerUnit));
  }

  /**
   * @type {{x: number, y: number, props: Object, component: Object}[]}
   */
  _newItems = [];

  /**  functions for adding new items */
  newGridItem(component, props, x = 0, y = 0, w = 1, h = 1) {
    const newItem = {
      id: generateUUID(),
      x: x,
      y: y,
      w: w,
      h: h,
      props: props,
      component: component,
    };
    console.log(" this._gridItemRefs", this._gridItemRefs);
    let pos = GridResolver.findSuitablePosition(
      newItem,
      this.gridItemRefs,
      this.xCount,
      this.yCount,
    );

    if (pos == null) {
      //display error popup
      toast.push("No space for new item", "error");
      return null;
    } else {
      newItem.x = pos.x;
      newItem.y = pos.y;
    }

    this._newItems.push(newItem);

    this.newItems = this._newItems;
    this.newGridItemCallback(this._newItems);
  }

  newGridItemCallback = () => {};

  setNewGridItemCallback(callback) {
    this.newGridItemCallback = callback;
  }

  set newItems(value) {
    this._newItems = value;
    this.newGridItemCallback(this.newItems);
  }

  get newItems() {
    return this._newItems;
  }

  serialize() {
    return JSON.stringify(this._gridItemRefs);
  }
}
