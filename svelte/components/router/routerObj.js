/**
 * Represents a Router object that manages and resolves site routes.
 * @class
 */
export class RouterObj {
  constructor() {
    this.routes = {};
  }

  /**
   * Function sets a prefix for all other nested routes
   * prefix: /dashboard -> all other routes are ->  dashboard/something
   * @param {string} prefix prefix of all other nested routes
   * @param {function} callback another nested routes belongs here
   * @returns {boolean}
   */
  group(prefix, callback) {
    const groupRouter = new RouterObj();
    callback(groupRouter);
    this.routes[prefix] = groupRouter.routes;
  }

  /**
   * Function checks route against siteRoute and handles its special form
   * Example:
   * siteRoute: /dashboard/E8YRO897AS, route dashboard/*
   * @param {string} siteRoute currentRoute of the site
   * @param {string} route route to check againsts
   * @returns {boolean}
   */
  static regexRoute(siteRoute, route) {
    const siteRouteParts = siteRoute.split("/").filter(Boolean); // filter(Boolean) is used to remove any empty strings
    const routeParts = route.split("/").filter(Boolean);

    if (routeParts.length !== siteRouteParts.length && !route.includes("**")) {
      return false;
    }

    const regex = new RegExp("{.*}");
    for (let i = 0; i < routeParts.length; i++) {
      if (routeParts[i] === "**") {
        return true;
      }
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
  getPropsBy(siteRoute, route) {
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
   * Defines a leaf route
   * @param path route to target
   * @param {svelteHTML} target target svelte component that will be rendered
   */
  get(path, target) {
    this.routes[path] = target;
  }

  /**
   * Flattens a nested route structure into an array of routes.
   *
   * @param {Object} routes - Nested routes object with paths as keys and values as page names or nested routes.
   * @returns {Array} Array of flattened routes with properties: `route` (full path) and `page` (associated page/component).
   */
  collapse(routes) {
    let collapsedRoutes = this.collapseRec(routes);

    // to make all routes with variable at the end
    let outputRoutes = []
    for (const route of collapsedRoutes) {
      let fullPath = route.route;
      //if is to check whether path has {id} variable if so we put on the end
      if (fullPath.includes("{") || fullPath.includes("}") ) {
        outputRoutes.push(route);
      } else {
        outputRoutes.unshift(route);
      }
    }
    return outputRoutes;
  }
  
  
  /**
   * Recursive part of collapse function, it flattens a nested route structure into an array of routes.
   *
   * @param {Object} routes - Nested routes object with paths as keys and values as page names or nested routes.
   * @param {string} [parent=''] - Current base path for nested routes.
   * @returns {Array} Array of flattened routes with properties: `route` (full path) and `page` (associated page/component).
   */
  collapseRec(routes, parent = "") {
    let collapsedRoutes = [];

    for (const [route, value] of Object.entries(routes)) {
      const fullPath = parent + route;

      if (typeof value === "object") {
        // If the value is an object, it means it's a nested route.
        // So, we recursively call collapse() to flatten the nested route.
        collapsedRoutes = collapsedRoutes.concat(
          this.collapseRec(value, fullPath),
        )


      } else {
        // If the value is a string, it means it's a leaf route.
        // We can directly add it to the result list.
        collapsedRoutes.push({route: fullPath, page: value});

      }
    }

    return collapsedRoutes;
  }

  /**
   * Resolves the properties associated with a given site route.
   *
   * @param {string} siteRoute - The route from the site to be resolved.
   * @returns {Object} An object containing properties related to the resolved route.
   *                   If the route is not found, an empty object is returned.
   */
  resolveProps(siteRoute) {
    let resolvedRoute = null;
    const collapsedRoutes = this.collapse(this.routes);

    for (let route of collapsedRoutes) {
      if (RouterObj.regexRoute(siteRoute, route.route)) {
        resolvedRoute = route.route;
        break;
      }
    }

    if (resolvedRoute != null) {
      return this.getPropsBy(siteRoute, resolvedRoute);
    }

    return {};
  }

  /**
   * Resolves the given site route to its associated page. If no match is found, returns the default page.
   *
   * @param {string} siteRoute - The route from the site to be resolved.
   * @param {svelteHTML} defaultPage - The default page to return if no matching route is found.
   * @returns {svelteHTML} The page/component associated with the given site route or the default page if not found.
   */
  resolve(siteRoute, defaultPage) {
    let resolvedRoute = defaultPage;
    const collapsedRoutes = this.collapse(this.routes);
    console.log("collapsed ", collapsedRoutes);
    for (let route of collapsedRoutes) {
      if (RouterObj.regexRoute(siteRoute, route.route)) {
        resolvedRoute = route.page;
        break;
      }
    }

    return resolvedRoute;
  }
}
