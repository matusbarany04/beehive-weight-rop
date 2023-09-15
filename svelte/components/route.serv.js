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
 * Function checks route against siteRoute and handles its special form
 * Example:
 * siteRoute: /dashboard/E8YRO897AS, route dashboard/*
 * @param {string} siteRoute currentRoute of the site
 * @param {string} route route to check againsts
 * @returns {boolean}
 */
export function regexRoute(siteRoute, route) {
  const siteRouteParts = siteRoute.split("/").filter(Boolean); // filter(Boolean) is used to remove any empty strings
  const routeParts = route.split("/").filter(Boolean);

  if (routeParts.length !== siteRouteParts.length) {
    return false;
  }

  const regex = new RegExp("{.*}");
  for (let i = 0; i < routeParts.length; i++) {
    if (
      !(routeParts[i] === "*" || regex.test(routeParts[i])) &&
      routeParts[i] !== siteRouteParts[i]
    ) {
      return false;
    }
  }

  return true;
}

/**
 * Extracts named parameters from a site route based on the route pattern.
 *
 * @function
 * @name getPropsBy
 * @param {string} siteRoute - The actual route, e.g. "/user/123/profile".
 * @param {string} route - The route pattern with named parameters, e.g. "/user/{id}/profile".
 * @returns {Object} An object containing the named parameters and their corresponding values.
 *
 * @example
 *   const props = getProps("/user/123/profile", "/user/{id}/profile");
 *   // props would be: { id: "123" }
 */
function getPropsBy(siteRoute, route) {
  const siteRouteParts = siteRoute.split("/").filter(Boolean); // filter(Boolean) is used to remove any empty strings
  const routeParts = route.split("/").filter(Boolean);

  let props = {};

  const regex = new RegExp("{.*}");
  for (let i = 0; i < routeParts.length; i++) {
    if (regex.test(routeParts[i])) {
      const varName = routeParts[i].replace(/{([^}]+)}/g, "$1");
      props[varName] = siteRouteParts[i];
    }
  }
  return props;
}

/**
 * Retrieves the named parameters from the current browser path based on the specified route pattern.
 *
 * @function
 * @name getCurrentProps
 * @param {string} route - The route pattern with named parameters, e.g. "/user/{id}/profile".
 * @returns {Object} An object containing the named parameters and their corresponding values from the current browser path.
 *
 * @example
 *   // Assuming the current browser path is "/user/123/profile"
 *   const props = getCurrentProps("/user/{id}/profile");
 *   // props would be: { id: "123" }
 */
export function getCurrentProps(route) {
  return getPropsBy(window.location.pathname, route);
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
