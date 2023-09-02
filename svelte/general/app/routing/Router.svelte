<script>
  import { route, navigate } from '../../../components/route.serv';
  import Login from "../../pages/Login.svelte";
  import Notfound from "../../../dashboard/app/pages/Notfound.svelte";
  import TestGeneralSite from "../../pages/TestGeneralSite.svelte";
  
  let value = Notfound;

  const BASE_PATH = "/web";

  route.subscribe(val => {
    switch(val) {
      case BASE_PATH + '/login':
        value = Login;
        break;
      case BASE_PATH + '/test':
        value = TestGeneralSite;
        break;
      default:
        value = Notfound;
    }
  });
</script>

<main>
  <svelte:component this={value}/>
</main>