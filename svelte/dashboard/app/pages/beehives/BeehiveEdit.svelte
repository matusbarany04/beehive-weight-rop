<script>
  
import SettingsItem from "../../component/settings/SettingsItem.svelte";
</script>

<h1>
  Upraviť váhu
</h1>

<div>
  <form action="/dashboardApi/saveBeehive" method="post">

    <SettingsItem title="Názov váhy">
      <input
        type="text"
        placeholder="Váš email"
        class="h-8 w-96 rounded-md border-2 border-slate-300 px-4"
      />
      
    </SettingsItem>
    
    <SettingsItem title="Poloha">
     
    </SettingsItem>
    
    
  </form>
</div>

