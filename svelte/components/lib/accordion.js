export function accordion(node, isOpen) {
  let initialHeight = node.offsetHeight;
  node.style.height = isOpen ? "auto" : 0 + "px";
  node.style.overflow = "hidden";
  return {
    update(isOpen) {
      let animation = node.animate(
        [
          {
            height: initialHeight + "px",
            overflow: "hidden",
          },
          {
            height: 0 + "px",
            overflowY: "hidden",
          },
        ],
        { duration: 100, fill: "both" },
      );
      animation.pause();
      if (!isOpen) {
        animation.play();
      } else {
        animation.reverse();
      }
    },
  };
}
