<script>
  import Router from './app/routing/Router.svelte';
  import TestComponent from "../components/TestComponent.svelte";
  import {prefix} from "../components/prefix";

  prefix.setPrefix('/web')
</script>

<style>
  @import 'tailwindcss/base';
  @import 'tailwindcss/components';
  @import 'tailwindcss/utilities';

</style>

<div class="">
    <Router/>
</div>