<script>
	import Router from './app/routing/Router.svelte';
	import Sidenav from './app/component/Sidenav.svelte';
    import {prefix} from "../components/prefix";

    prefix.setPrefix('/dashboard');
</script>

<style>
  @import 'tailwindcss/base';
  @import 'tailwindcss/components';
  @import 'tailwindcss/utilities';

</style>

<div class="bg-slate-300">
  <Sidenav class="sidenav" />
  <Router />
</div>