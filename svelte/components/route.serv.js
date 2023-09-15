/**
 * This module provides utilities for routing and navigation within a Svelte application.
 * It exports a writable Svelte store (`route`) to keep track of the current route,
 * as well as helper functions to navigate programmatically.
 */
import { writable } from "svelte/store";
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
  window.history.pushState({}, "", path);
  route.set(path);
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
