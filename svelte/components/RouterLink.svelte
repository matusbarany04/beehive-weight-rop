<script>
  import { navigate } from './route.serv';
  export let url;

  function changeRoute(event) {
    navigate(event.currentTarget.getAttribute('href'));
  }
</script>


<a class="text-confirm-500" href="{url}" on:click|preventDefault={changeRoute}>
  <slot></slot>
</a>