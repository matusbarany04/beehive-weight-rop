<script>
	import Router from './app/routing/Router.svelte';
	import Sidenav from './app/component/Sidenav.svelte';
    import {prefix} from "../components/prefix";
    import Panel from "../components/components/dashboard/panel/Panel.svelte";

    prefix.setPrefix('/dashboard');
</script>

<style>
  @import 'tailwindcss/base';
  @import 'tailwindcss/components';
  @import 'tailwindcss/utilities';

</style>

<div class="bg-slate-300">
<!--  <Sidenav class="sidenav" />-->
<!--   <Panel></Panel>-->
  <Router />
</div>