<script>
  import { route } from "../../../components/router/route.serv";
  import Login from "../../pages/Login.svelte";
  import Notfound from "../../pages/Notfound.svelte";
  import Home from "../../pages/Home.svelte";
  import TestGeneralSite from "../../pages/TestGeneralSite.svelte";
  import Register from "../../pages/Register.svelte";
  import AccountCreated from "../../pages/AccountCreated.svelte";
  import { RouterObj } from "../../../components/router/routerObj";
  import Shop from "../../pages/Shop.svelte";
  import FaQ from "../../pages/FaQ.svelte";
  import Contact from "../../pages/Contact.svelte";
  import About from "../../pages/About.svelte";
  import LoginAlt from "../../pages/LoginAlt.svelte";
  import ScrollTest from "../../pages/ScrollTest.svelte";

  let value = Notfound;
  let props = {};

  route.subscribe((currentRoute) => {
    const routes = new RouterObj();

    routes.get("/", Home);
    routes.get("/login", LoginAlt);
    routes.get("/register", Register);
    routes.get("/accountCreated", AccountCreated);
    routes.get("/test", TestGeneralSite);
    routes.get("/scroll", ScrollTest);
    routes.get("/contact", Contact);
    routes.get("/about", About);
    routes.get("/shop", Shop);
    routes.get("/faq", FaQ);
    value = routes.resolve(currentRoute, Notfound);

    props = routes.resolveProps(currentRoute);
  });
</script>

<svelte:component this={value} {props} />
