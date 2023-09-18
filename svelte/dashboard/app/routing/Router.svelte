<script>
  import { route } from "../../../components/router/route.serv";
  import Homepage from "../pages/Homepage.svelte";
  import Notfound from "../pages/Notfound.svelte";
  import Help from "../pages/Help.svelte";
  import Settings from "../pages/Settings.svelte";
  import DashLayout from "../layouts/DashLayout.svelte";
  import MainLayout from "../layouts/MainLayout.svelte";
  import Calendar from "../pages/Calendar.svelte";
  import Test from "../pages/Test.svelte";
  import Beehive from "../pages/beehives/Beehive.svelte";
  import Beehives from "../pages/Beehives.svelte";
  import Notifications from "../pages/Notifications.svelte";
  import { RouterObj } from "../../../components/router/routerObj";
  import BeehiveAdd from "../pages/beehives/BeehiveAdd.svelte";
  import BeehiveToken from "../pages/beehives/BeehiveToken.svelte";
  import BeehiveEdit from "../pages/beehives/BeehiveEdit.svelte";

  const BASE_PATH = "/dashboard";
  let value = Notfound;

  let props = {};

  route.subscribe((currentRoute) => {
    const routes = new RouterObj();

    routes.group(BASE_PATH, (root) => {
      root.get("/", Homepage);
      root.get("/homepage", Homepage);
      root.get("/help", Help);
      root.get("/settings", Settings);
      root.get("/calendar", Calendar);
      root.get("/test", "Test");
      root.get("/notifications", Notifications);

      root.group("/beehives", (beeGroup) => {
        beeGroup.get("/", Beehives);

        beeGroup.group("/add", (addGroup) => {
          addGroup.get("", Beehive);
          addGroup.get("/token", BeehiveToken);
        });

        beeGroup.group("/{id}", idGroup => {
          idGroup.get("", Beehive);
          idGroup.get("/edit", BeehiveEdit);
        }); // Dynamic route
      });
    });

    value = routes.resolve(currentRoute, Notfound);

    props = routes.resolveProps(currentRoute);
  });
</script>

<MainLayout>
  <DashLayout>
    <svelte:component this={value} {props} />
  </DashLayout>
</MainLayout>
