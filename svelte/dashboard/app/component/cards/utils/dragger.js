export class Dragger {
  /**
   *
   * @param node {HTMLElement}
   * @param item {Item}
   * @param root {HTMLElement}
   * */
  constructor(node, item, root) {
    this.node = node;
    this.isDraggable = false;
    this.isDragging = false;
    this.offsetX = 0;
    this.offsetY = 0;
    this.item = item;
    this.root = root;

    // Bind methods to the instance to preserve `this` context
    this.handleMouseDown = this.handleMouseDown.bind(this);
    this.handleMouseMove = this.handleMouseMove.bind(this);
    this.handleMouseUp = this.handleMouseUp.bind(this);

    this.init();
  }

  init() {
    this.node.addEventListener("mousedown", this.handleMouseDown);
  }

  /**
   *
   * @param value {boolean}
   */
  setDraggable(value) {
    this.isDraggable = value;
  }

  handleMouseDown(event) {
    if (this.isDraggable) {
      this.isDragging = true;

      // Calculate the initial offset at the start of the drag
      this.offsetX = event.clientX - this.node.offsetLeft;
      this.offsetY = event.clientY - this.node.offsetTop;

      this.mouseDownEvent(event);
      document.addEventListener("mousemove", this.handleMouseMove);
      document.addEventListener("mouseup", this.handleMouseUp);
      document.addEventListener("wheel", this.handleMouseWheel, {
        passive: false,
      });
    }
  }

  handleMouseMove(event) {
    if (!this.isDragging) return;

    const x = event.clientX - this.offsetX;
    const y = event.clientY - this.offsetY;

    this.item.xCoordinate = x + this.root.scrollTop;
    this.item.yCoordinate = y + this.root.scrollLeft;

    let relativePos = this.getMouseToItemPosition(event);

    this.mouseMoveEvent(x + relativePos.x, y + relativePos.y);
  }

  getMouseToItemPosition(event) {
    return {
      /** @type {int} */
      x: event.clientX - this.node.getBoundingClientRect().left,
      /** @type {int} */
      y: event.clientY - this.node.getBoundingClientRect().top,
    };
  }

  handleMouseWheel(event) {
    if (this.isDragging) {
      event.preventDefault();
    }
  }

  handleMouseUp(event) {
    this.isDragging = false;
    this.mouseUpEvent(event);

    // Remove the event listeners to prevent unnecessary operations
    document.removeEventListener("mousemove", this.handleMouseMove);
    document.removeEventListener("mouseup", this.handleMouseUp);
    document.removeEventListener("wheel", this.handleMouseWheel);
  }

  mouseUpEvent = () => {};

  setOnMouseUpEvent(event) {
    this.mouseUpEvent = event;
  }

  mouseMoveEvent = (x, y) => {};

  setOnMouseMoveEvent(event) {
    this.mouseMoveEvent = event;
  }

  mouseDownEvent = () => {};

  setOnMouseDownEvent(event) {
    this.mouseDownEvent = event;
  }
}
