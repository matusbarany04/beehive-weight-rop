<script>
	import Router from './app/routing/Router.svelte';
	import Sidenav from './app/component/Sidenav.svelte';
    import {prefix} from "../components/prefix";
    import Panel from "../components/components/dashboard/panel/Panel.svelte";

    prefix.setPrefix('/dashboard');
</script>

<style>
  @import url("https://fonts.googleapis.com/css2?family=Bree+Serif&display=swap");
  @import url("https://fonts.googleapis.com/css2?family=Jost&display=swap");
  @import url("https://fonts.googleapis.com/css2?family=Inter:wght@100;200;300;400;500;600;700;800;900&family=Open+Sans:wght@300;400;600;700&display=swap");
  @import url("https://fonts.googleapis.com/css2?family=Exo+2:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap"); /*  futuristicke */

  @import url("https://fonts.googleapis.com/css2?family=Rubik:ital,wght@0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap");

  @import 'tailwindcss/base';
  @import 'tailwindcss/components';
  @import 'tailwindcss/utilities';

  :root {
    padding: 0;
    margin: 0;
    --font-body: "Rubik", -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto,
    Oxygen, Ubuntu, Cantarell, "Open Sans", "Helvetica Neue", sans-serif;
    --font-mono: "Fira Mono", monospace;
    --color-primary: #161819;
    --color-secondary: #e2a70f;
    /* --color-tertiary: #F7F7F7; */
    --color-tertiary: #e0e3e8; /*  original one dont delete */
    --color-text-light: #000000;
    --color-text-dark: #b9beca;
    --color-accept: #52b515;
    --color-delete: #e2360f;
    --color-edit: #e2d00f;
    --color-detail-gray: #8d8888;
    --column-width: 48rem;
    font-family: var(--font-body);
    color: var(--color-text-light);
  }

  body {
    min-height: 100vh;
    margin: 0;
    background-attachment: fixed;
    background-size: 100vw 100vh;
  }

  .shadow {
    box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
  }

  ::-webkit-scrollbar {
    width: 5px;
    height: 7px;
  }

  ::-webkit-scrollbar-thumb {
    background-color: #1e202a;
    outline: 1px solid #1e202a;
    border-radius: 50px;
  }

  ::-webkit-scrollbar-track {
    box-shadow: inset 0 0 4px rgba(0, 0, 0, 0.2);
  }

  @-moz-document url-prefix() {
    .scroller {
      scrollbar-width: thin;
      scrollbar-color: #1e202a;
    }
  }

</style>

<div class="bg-slate-300">
<!--  <Sidenav class="sidenav" />-->
<!--   <Panel></Panel>-->
  <Router />
</div>