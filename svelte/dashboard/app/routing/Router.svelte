<script>
  import { route, navigate } from '../../../components/route.serv';
  import Homepage from '../pages/Homepage.svelte';
  import Notfound from '../pages/Notfound.svelte';
  import Help from '../pages/Help.svelte';

  const BASE_PATH = "/dashboard";
  let value = Notfound;

  route.subscribe(val => {
    switch(val) {
      case BASE_PATH + '/':
      case BASE_PATH + '':
      case BASE_PATH +  '/homepage':
        value = Homepage;
        break;
      case BASE_PATH +  '/help':
      case BASE_PATH +  '/help/':
        value = Help;
        break;
      default:
        value = Notfound;
    }
  });
</script>

<main>
  <svelte:component this={value}/>
</main>