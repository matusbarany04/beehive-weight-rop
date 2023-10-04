import { generateUUID } from "../../../../../components/lib/utils/staticFuncs";

export class Item {
  id;
  _x; //in relative pos
  _y;
  _w;
  _h;
  _xCoordinate; // inPixelValues
  _yCoordinate;
  _pixelWidth;
  _pixelHeight;
  _unitSize = 1;
  _mounted = false;
  _draggable = false;

  constructor(x, y, w, h) {
    this.id = generateUUID();
    this._x = x;
    this._y = y;
    this._w = w;
    this._h = h;
    this.calculateSize();
  }

  set unitSize(value) {
    this._unitSize = value;
    this.calculateSize();
    this.callback();
  }

  calculateSize() {
    this._pixelHeight = this._unitSize * this._h;
    this._pixelWidth = this._unitSize * this._w;
  }

  calculatePos(padding) {
    this._xCoordinate = this._unitSize * this._x;
    this._yCoordinate = this._unitSize * this._y;
  }

  get x() {
    return this._x;
  }

  get pixelHeight() {
    return this._pixelHeight;
  }

  /**
   *
   * @param value {boolean}
   */
  set mounted(value) {
    this._mounted = value;
    this.mountedChangedEvent();
  }

  /**
   *
   * @return {boolean}
   */
  get mounted() {
    return this._mounted;
  }

  mountedChangedEvent = () => {};

  subscribeMounted(event) {
    this.mountedChangedEvent = event;
  }

  set pixelHeight(value) {
    this._pixelHeight = value;
    this.callback();
  }

  get pixelWidth() {
    return this._pixelWidth;
  }

  set pixelWidth(value) {
    this._pixelWidth = value;
    this.callback();
  }

  set x(value) {
    this._x = value;
    this.callback();
    this.calculateSize();
  }

  get y() {
    return this._y;
  }

  set y(value) {
    this._y = value;
    this.callback();
    this.calculateSize();
  }

  get w() {
    return this._w;
  }

  set w(value) {
    this._w = value;
    this.callback();
    this.calculateSize();
  }

  get h() {
    return this._h;
  }

  set h(value) {
    this._h = value;
    this.callback();
    this.calculateSize();
  }

  get xCoordinate() {
    return this._xCoordinate;
  }

  set xCoordinate(value) {
    this._xCoordinate = value;
    this.callback();
  }

  get yCoordinate() {
    return this._yCoordinate;
  }

  set yCoordinate(value) {
    this._yCoordinate = value;
    this.callback();
  }

  callback = () => {};

  setValueChangedCallback(callback) {
    this.callback = callback;
  }

  /**
   *
   * @param value {boolean}
   */
  set draggable(value) {
    this._draggable = value;
    this.draggableChangedEvent(this._draggable);
  }

  get draggable() {
    return this._draggable;
  }

  draggableChangedEvent = () => {};
  setDraggableChangedEvent(event) {
    this.draggableChangedEvent = event;
  }
}
