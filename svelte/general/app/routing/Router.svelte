<script>
  import { route } from "../../../components/route.serv";
  import Login from "../../pages/Login.svelte";
  import Notfound from "../../pages/Notfound.svelte";
  import Home from "../../pages/Home.svelte";
  import TestGeneralSite from "../../pages/TestGeneralSite.svelte";
  import Register from "../../pages/Register.svelte";
  import AccountCreated from "../../pages/AccountCreated.svelte";
  import { RouterObj } from "../../../components/router/routerObj";

  let value = Notfound;
  let props = {};

  route.subscribe((currentRoute) => {
    const routes = new RouterObj();

    routes.get("/", Home);
    routes.get("/login", Login);
    routes.get("/register", Register);
    routes.get("/accountCreated", AccountCreated);
    routes.get("/test", TestGeneralSite);

    value = routes.resolve(currentRoute, Notfound);

    props = routes.resolveProps(currentRoute);
  });
</script>

<main>
  <svelte:component this={value} {props} />
</main>
