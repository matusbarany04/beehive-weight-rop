<script>
  import { route, navigate } from '../../../components/route.serv';
  import Login from "../../pages/Login.svelte";
  import Notfound from "../../../dashboard/app/pages/Notfound.svelte";
  import Home from "../../pages/Home.svelte";
  
  let value = Notfound;
  


  route.subscribe(val => {
    switch(val) {
      case '/':
        value = Home;
        break;
        
      case '/login':
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