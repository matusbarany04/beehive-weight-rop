import {get, writable} from "svelte/store";
import {setOpacity} from "leaflet/src/dom/DomUtil";

let opened = writable(true)


export default {
    setOpened(value) {
        opened.set(value);
    },
    isOpened() {
        return get(opened);
    },
    getOpenedRef() {
        return opened;
    },
    toggleOpened() {
        opened.update((b) => !b);
    }
}