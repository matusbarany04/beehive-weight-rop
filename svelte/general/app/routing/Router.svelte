<script>
  import { route } from '../../../components/route.serv';
  import Login from "../../pages/Login.svelte";
  import Notfound from "../../../dashboard/app/pages/Notfound.svelte";
  import Home from "../../pages/Home.svelte";
  import TestGeneralSite from "../../pages/TestGeneralSite.svelte";
  import Register from "../../pages/Register.svelte";
  import AccountCreated from "../../pages/AccountCreated.svelte";
  let value = Notfound;

  route.subscribe(val => {
    switch(val) {
      case '/':
        value = Home;
        break;
      case '/login':
        value = Login;
        break;
      case '/register':
        value = Register;
        break;
      case '/accountCreated':
        value = AccountCreated;
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