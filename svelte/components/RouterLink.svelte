<script>
  import { prefix } from "./router/prefix.js";
  import { navigate } from "./router/route.serv";

  /**
   * The URL or path to which the link should point within the SPA (Single Page Application).
   *
   * Note: This is designed for JavaScript-based routing within the application.
   * If you wish to navigate outside the SPA or to an external link, consider using a regular `<a>` tag.
   *
   * @type {String}
   * @required
   */
  export let url;

  /**
   * Determines if navigation should force a page reload.
   *
   * @type {Boolean}
   * @default false
   */
  export let reload = false;

  /**
   * Determines if the dynamic prefix should be bypassed.
   * When set to true, the dynamic prefix stored in the prefix store is ignored.
   * Useful for base or root routes.
   * @type {Boolean}
   * @default false
   */
  export let baseRoute = false;

  /**
   * Action that is run before redirect to url address
   * @type {function}
   * @default empty function
   */
  export let action = function () {};

  export let append = false;

  let currentPrefix = "";

  prefix.subscribe((value) => {
    currentPrefix = value || "";
  });

  function changeRoute(event) {
    action();
    if (!reload) {
      console.log("router link not reloading!");
      navigate(event.currentTarget.getAttribute("href"));
    } else {
      // window.location.href = event.currentTarget.getAttribute("href");
    }
  }
</script>

<a
  class="text-slate-500"
  href="{!baseRoute && !append ? currentPrefix : ''}{append
    ? location.pathname
    : ''}{url}"
  on:click|preventDefault={changeRoute}
>
  <slot />
</a>
