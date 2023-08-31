<script>
  import { route, navigate } from '../services/route.serv';
  import Homepage from '../pages/Homepage.svelte';
  import Notfound from '../pages/Notfound.svelte';

  const BASE_PATH = "/dashboard";
  let value = Notfound;

  route.subscribe(val => {
    switch(val) {
      case BASE_PATH + '/':
      case BASE_PATH +  '/homepage':
        value = Homepage;
        break;
      default:
        value = Notfound;
    }
  });
</script>

<style>
  main {
    flex-grow: 1;
    background-color: var(--primary-color-2);
    color: var(--second-color);
    padding: 1rem;
  }
</style>

<main>
  <svelte:component this={value}/>
</main>