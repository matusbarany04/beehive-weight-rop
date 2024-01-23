<script>
  /**
   * @fileoverview This page is for resolving user related problem and providing explanations
   * @module Help
   */
  import HelpDropdown from "../../../components/help/HelpDropdown.svelte";
  import SettingsHeader from "../component/settings/SettingsHeader.svelte";

  import message from "../stores/message";

  message.setMessage("Pomoc");
</script>

<svelte:head>
  <title>Pomoc</title>
  <meta
    name="help"
    content="This page is for resolving user related problem and providing explanations"
  />
</svelte:head>

<div class="grid h-full w-full place-items-center">
  <p>Caka sa na spravenie</p>
</div>
