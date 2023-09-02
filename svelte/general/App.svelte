<script>
  import Router from './app/routing/Router.svelte';
  import TestComponent from "../components/TestComponent.svelte";
</script>

<style>
  @import 'tailwindcss/base';
  @import 'tailwindcss/components';
  @import 'tailwindcss/utilities';

</style>

<div class="bg-slate-300">
    <Router/>
</div>