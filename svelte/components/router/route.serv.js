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
  currentPrefix = value || "";
});

/**
 * Get the depth of the current route.
 * @returns {number} The depth of the route.
 */
export function getRouteDepth() {
  const routeSegments = get(route)
    .replace(/^\/|\/$/g, "")
    .split("/");
  const nonEmptySegments = routeSegments.filter(
    (segment) => segment.trim() !== "",
  );
  return nonEmptySegments.length;
}

/**
 * Traverse back a certain number of levels in the route.
 * @param {number} levels - The number of levels to traverse back.
 */
export function traverseBack(levels) {
  // get original route
  const path = get(route);
  // split to segments
  const segments = path.split("/").filter((segment) => segment.trim() !== "");
  // calculate new length
  const newLength = Math.max(0, segments.length - levels);
  // make new route
  let newRoute = `/${segments.slice(0, newLength).join("/")}`;
  // navigate to new route
  navigate(newRoute);
}

/**
 * Param holder and adjacent functions
 */
let urlParams = new URLSearchParams(window.location.search);

export function pushParam(paramName, value) {
  urlParams.set(paramName, value);
  updateUrl();
}

export function removeParam(paramName) {
  urlParams.delete(paramName);
  updateUrl();
}

/**
 *
 * @param paramName
 * @returns {string}
 */
export function getParamValue(paramName) {
  return urlParams.get(paramName);
}

export function isParamSet(name) {
  return urlParams.has(name);
}

function resetParams() {
  urlParams = new URLSearchParams(); // Resets the URLSearchParams object
  updateUrl();
}

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
  window.history.pushState({}, "", `${path}?${urlParams}`);

  route.set(path);

  //clear the data state to not interfere with other pages
  resetUnsavedData();
}
function updateUrl() {
  window.history.replaceState({}, "", `${get(route)}?${urlParams}`);
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
  areUnsavedData.set(false);
  if (bool) {
    window.history.pushState("{}", "", location.path);
    const forms = document.getElementsByTagName("form");
    for (let form of forms)
      form.addEventListener("input", () => {
        areUnsavedData.set(true);
        console.log("changed");
      });

    handleRefresh();
    window.onpopstate = (e) => {
      if (
        !areThereUnsavedData() ||
        confirm("You have unsaved changes! Are you sure you want to leave?")
      ) {
        setUnsavedData(false);
        window.onpopstate = undefined;
        window.onbeforeunload = undefined;
        history.back();
      }

      window.history.pushState("{}", "", location.path);
    };
  } else window.onpopstate = window.onbeforeunload = undefined;
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

function handleRefresh() {
  window.onbeforeunload = (e) => {
    history.back();
    window.onpopstate = undefined;
    window.onbeforeunload = undefined;
    if (areThereUnsavedData()) {
      setUnsavedData(false);
      // Custom message is not always displayed due to browser limitations
      // Browsers might display their own default message instead
      const message =
        "You have unsaved changes! Are you sure you want to leave?";
      e.returnValue = message; // Gecko, Trident, Chrome earlier than 51
      return message; // Gecko, WebKit, Chrome from 51
    }
  };
}

// Create a writable store with an initial value of an empty array
const callbacks = writable([]);

export function setOnAfterNavigate(callback) {
  console.log("setOnAfterNavigate");
  // Use the update method to push a new callback to the callbacks array
  callbacks.update((currCallbacks) => [...currCallbacks, callback]);
}

/**
 * Iterates over the callbacks and calls them.
 */
export async function callAfterNavigateCallbacks() {
  await tick();
  // Use the value from the callbacks store
  console.log("callingAfterNavigate");
  callbacks.subscribe((currCallbacks) => {
    for (const callback of currCallbacks) {
      callback();
    }
  })();
}
