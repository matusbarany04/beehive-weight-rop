export class Resizer {
  /**
   *
   * @param node {HTMLElement}
   * @param item {Item}
   * @param root {Object}
   * */
  constructor(node, item, resizableObject, root) {
    this.node = node;
    this.isResizable = false;
    this.isResizing = false;
    this.startWidth = 0;
    this.startHeight = 0;
    this.item = item;
    this.root = root;
    this.resizableObject = resizableObject;

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
  setResizable(value) {
    this.isResizable = value;
  }

  handleMouseDown(event) {
    // stop dragger from picking up the event
    event.stopPropagation();

    if (this.isResizable) {
      //setting shadow behind the item to show possible positioning
      this.root.setShadow(this.item);

      this.isResizing = true;

      // Store the initial dimensions at the start of the resize
      this.startWidth = this.resizableObject.offsetWidth;
      this.startHeight = this.resizableObject.offsetHeight;

      this.startX = event.clientX;
      this.startY = event.clientY;

      this.mouseDownEvent(event);
      document.addEventListener("mousemove", this.handleMouseMove);
      document.addEventListener("mouseup", this.handleMouseUp);
      document.addEventListener("wheel", this.handleMouseWheel, {
        passive: false,
      });
    }
  }

  handleMouseMove(event) {
    if (!this.isResizing) return;

    this.root.updateShadow();

    const widthDelta = event.clientX - this.startX;
    const heightDelta = event.clientY - this.startY;

    this.item.pixelWidth = this.startWidth + widthDelta;
    this.item.pixelHeight = this.startHeight + heightDelta;

    this.mouseMoveEvent(this.item.w, this.item.h);
  }

  handleMouseWheel(event) {
    if (this.isResizing) {
      event.preventDefault();
    }
  }

  handleMouseUp(event) {
    this.isResizing = false;
    this.root.resetShadow();
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

  mouseMoveEvent = (width, height) => {};

  setOnMouseMoveEvent(event) {
    this.mouseMoveEvent = event;
  }

  mouseDownEvent = () => {};

  setOnMouseDownEvent(event) {
    this.mouseDownEvent = event;
  }
}
