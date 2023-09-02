<script>
  import { route, navigate } from '../services/route.serv';
  import Login from "../../pages/Login.svelte";
  import Notfound from "../../../dashboard/app/pages/Notfound.svelte";
  
  let value = Notfound;

  const BASE_PATH = "/web";

  route.subscribe(val => {
    switch(val) {
      case BASE_PATH + '/login':
        value = Login;
        break;
      default:
        value = Notfound;
    }
  });
</script>

<main>
  <svelte:component this={value}/>
</main>