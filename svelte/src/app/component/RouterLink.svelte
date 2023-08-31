<script>
  import { navigate } from '../services/route.serv.js';
  export let url;

  function changeRoute(event) {
    navigate(event.currentTarget.getAttribute('href'));
  }
</script>

<style>
  a {
    color: var(--second-color-4);
  }
</style>

<a href="/dashboard/{url}" on:click|preventDefault={changeRoute}>
  <slot></slot>
</a>