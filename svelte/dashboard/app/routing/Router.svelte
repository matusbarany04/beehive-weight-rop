<script>
  import {
    getCurrentProps,
    regexRoute,
    route,
  } from "../../../components/route.serv";
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

  const BASE_PATH = "/dashboard";
  let value = Notfound;

  let props = {};

  route.subscribe((val) => {
    console.log(val);
    switch (val) {
      case BASE_PATH + "/":
      case BASE_PATH + "":
      case BASE_PATH + "/homepage":
        value = Homepage;
        break;
      case BASE_PATH + "/help":
      case BASE_PATH + "/help/":
        value = Help;
        break;
      case BASE_PATH + "/settings":
      case BASE_PATH + "/settings/":
        value = Settings;
        break;
      case BASE_PATH + "/calendar":
      case BASE_PATH + "/calendar/":
        value = Calendar;
        break;
      case BASE_PATH + "/test":
      case BASE_PATH + "/test/":
        value = Test;
        break;
      case BASE_PATH + "/beehives":
        value = Beehives;
        break;
      case BASE_PATH + "/notifications":
      case BASE_PATH + "/notifications/":
        value = Notifications;
        break;
      default:
        if (regexRoute(val, BASE_PATH + "/beehive/{id}")) {
          props = getCurrentProps(BASE_PATH + "/beehive/{id}");
          value = Beehive;
          break;
        }

        value = Notfound;
    }
  });
</script>

<MainLayout>
  <DashLayout>
    <svelte:component this={value} {props} />
  </DashLayout>
</MainLayout>
