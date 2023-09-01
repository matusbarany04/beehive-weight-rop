<script>
  import { navigate } from '../services/route.serv.js';
  export let url;

  function changeRoute(event) {
    navigate(event.currentTarget.getAttribute('href'));
  }
</script>


<a class="text-confirm-500" href="/dashboard/{url}" on:click|preventDefault={changeRoute}>
  <slot></slot>
</a>