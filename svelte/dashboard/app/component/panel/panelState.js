import { get, writable } from "svelte/store";
import { setOpacity } from "leaflet/src/dom/DomUtil";

let opened = writable(true);

let mode = writable("default");

export default {
  /**
   *
   * @param value {boolean}
   */
  setOpened(value) {
    opened.set(value);
  },
  /**
   * @return {boolean}
   */
  isOpened() {
    return get(opened);
  },
  /**
   *
   * @return {writable}
   */
  getOpenedRef() {
    return opened;
  },
  toggleOpened() {
    opened.update((b) => !b);
  },
  /**
   *
   * @param mode {string}
   */
  setMode(newMode) {
    mode.set(newMode);
  },
  /**
   * @return {string}
   */
  getMode() {
    return get(mode);
  },
  /**
   * @return {writable}
   */
  getModeRef() {
    return mode;
  },
  /**
   *
   */
  resetMode() {
    mode.set("default");
  },
};
