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
  import ChangePassword from "../pages/ChangePassword.svelte";
  import BeehiveSendAction from "../pages/beehives/BeehiveSendAction.svelte";

  const BASE_PATH = "/dashboard";
  let value = Notfound;

  let props = {};
  let key = 1;

  // export function invalidate() {
  //   key++;
  // }

  route.subscribe((currentRoute) => {
    const routes = new RouterObj();

    routes.group(BASE_PATH, (root) => {
      root.get("/", Homepage);
      root.get("/homepage", Homepage);
      root.get("/help", Help);
      root.groupGet("/settings", Settings, (settingsGroup) => {
        settingsGroup.get("/newpassword", ChangePassword);
      });
      root.get("/calendar", Calendar);
      root.get("/test", Test);
      root.get("/notifications", Notifications);

      root.group("/beehives", (beeGroup) => {
        beeGroup.get("/", Beehives);

        beeGroup.groupGet("/add", BeehiveAdd, (addGroup) => {
          addGroup.get("/token", BeehiveToken);
        });

        beeGroup.groupGet("/{id}", Beehive, (idGroup) => {
          idGroup.get("/edit", BeehiveEdit);
          idGroup.get("/action", BeehiveSendAction);
        }); // Dynamic route
      });
    });

    key++;

    value = routes.resolve(currentRoute, Notfound);

    props = routes.resolveProps(currentRoute);
  });
</script>

<MainLayout>
  <DashLayout>
    {#key key}
      <svelte:component this={value} {props} />
    {/key}
  </DashLayout>
</MainLayout>
