/**
 * This module provides utilities for routing and navigation within a Svelte application.
 * It exports a writable Svelte store (`route`) to keep track of the current route,
 * as well as helper functions to navigate programmatically.
 *
 * @module routerservjs
 */
import { get, writable } from "svelte/store";
import { prefix } from "./prefix.js";

/**
 * A writable Svelte store that holds the current application's route (path).
 * Initialized with the current window's location pathname.
 */
export const route = writable(window.location.pathname);

// Subscribe to prefix changes and update the `currentPrefix` accordingly.
let currentPrefix = "";
prefix.subscribe((value) => {
  console.log(value);
  currentPrefix = value || "";
});

/**
 * An event listener that updates the `route` store when the browser's back/forward buttons are used.
 */
window.addEventListener("popstate", () => {
  route.set(window.location.pathname);
});

/**
 * Navigates to a given path programmatically.
 *
 * @param {string} path - The path to navigate to.
 */
export function navigate(path) {
  // user canceled confirm popup, don't route
  if (areThereUnsavedData() && !unsavedDataPrompt()) {
    return;
  }
  //else route
  window.history.pushState({}, "", path);
  route.set(path);
  //clear the data state to not interfere with other pages
  resetUnsavedData();
}

/**
 * Navigates to a given path programmatically, but with the added `currentPrefix`.
 * Useful for apps with base path or dynamic segment in the route.
 *
 * @param {string} path - The path to navigate to after the prefix.
 */
export function navigateWithPrefix(path) {
  navigate(`${currentPrefix}${path}`);
}

let areUnsavedData = writable(false);

/**
 * If set to true, browser should display a popup, telling user that he has some unsaved data
 * @param bool state of data on the page
 */
export function setUnsavedData(bool) {
  areUnsavedData.set(bool);
}

/**
 * Sets unsavedData state to its default value - false
 */
export function resetUnsavedData() {
  areUnsavedData.set(false);
}

/**
 * Returns unsavedData state which indicates if user has unsaved data
 */
function areThereUnsavedData() {
  return get(areUnsavedData);
}

function unsavedDataPrompt() {
  return confirm("You have unsaved data! Are you sure you want to proceed?");
}

window.addEventListener("beforeunload", function (e) {
  if (areThereUnsavedData()) {
    // Custom message is not always displayed due to browser limitations
    // Browsers might display their own default message instead
    const message =
      "You have unsaved changes! Are you sure you want to leave?" +
      areThereUnsavedData();
    e.returnValue = message; // Gecko, Trident, Chrome earlier than 51
    return message; // Gecko, WebKit, Chrome from 51
  }
});
