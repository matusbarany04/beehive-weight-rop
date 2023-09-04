<script>
  import { route } from '../../../components/route.serv';
  import Homepage from '../pages/Homepage.svelte';
  import Notfound from '../pages/Notfound.svelte';
  import Help from '../pages/Help.svelte';
  import Settings from "../pages/Settings.svelte";
  import DashLayout from "../layouts/DashLayout.svelte";
  import MainLayout from "../layouts/MainLayout.svelte";
  import Calendar from "../pages/Calendar.svelte";

  const BASE_PATH = "/dashboard";
  let value = Notfound;

  route.subscribe(val => {
    console.log(val)
    switch(val) {
      // case BASE_PATH + '/':
      // case BASE_PATH + '':
      // case BASE_PATH +  '/homepage':
      //   value = Homepage;
      //   break;
      case BASE_PATH +  '/help':
      case BASE_PATH +  '/help/':
        value = Help;
        break;
      case BASE_PATH +  '/settings':
      case BASE_PATH +  '/settings/':
        value = Settings;
        break;
      case BASE_PATH +  '/calendar':
      case BASE_PATH +  '/calendar/':
        value = Calendar;
        break;
      default:
        value = Notfound;
    }
  });
</script>

<MainLayout>
  <DashLayout>
    <svelte:component this={value}/>
  </DashLayout>
</MainLayout>