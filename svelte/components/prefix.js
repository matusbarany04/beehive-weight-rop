import { writable } from "svelte/store";

// Initial value can be empty or whatever default prefix you want.
const initialPrefix = "";

const createPrefixStore = () => {
  const { subscribe, set, update } = writable(initialPrefix);

  return {
    subscribe,
    setPrefix: (newPrefix) => set(newPrefix),
    appendToPrefix: (additionalPrefix) => update((n) => n + additionalPrefix),
  };
};

export const prefix = createPrefixStore();
