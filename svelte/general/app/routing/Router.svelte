<script>
  import { route } from '../../../components/route.serv';
  import Login from "../../pages/Login.svelte";
  import Notfound from "../../../dashboard/app/pages/Notfound.svelte";
  import Home from "../../pages/Home.svelte";
  import TestGeneralSite from "../../pages/TestGeneralSite.svelte";
  let value = Notfound;

  route.subscribe(val => {
    switch(val) {
      case '/':
        value = Home;
        break;
        
      case '/login':
        value = Login;
        break;
      case '/test':
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