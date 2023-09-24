/**
 * @fileoverview Utility for loading general users settings
 * @module settingsLoader.js
 */

import { writable, get } from "svelte/store";
import shared, { loadData } from "./shared";

/**
 * Shared settings state
 */
let settings = writable({});

export default {
  /**
   * Function loads settings from a server
   * @returns void
   */
  loadSettings: async function () {
    let results = await fetch("/dashboardApi/settings/getData").then(
      async (result) => {
        const json = await result.json();
        return json["status"] === "ok" ? json["settings"] : undefined;
      },
    );
    // TODO remove log
    console.log(results);

    if (results.error === undefined) {
      // adding data to shared.onDataLoaded
      loadData("settings", results);
      settings.set(results);
    } else {
      console.error(
        "Error loading user settings: ",
        results.message,
        " ",
        results.error,
      );
    }
  },
};
