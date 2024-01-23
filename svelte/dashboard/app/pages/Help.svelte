<script>
  /**
   * @fileoverview This page is for resolving user related problem and providing explanations
   * @module Help
   */
  import message from "../stores/message";
  import { getLanguageInstance } from "../../../components/language/languageRepository";

  let li = getLanguageInstance();

  message.setMessage(li.get("help.page"));
</script>

<svelte:head>
  <title>{li.get("help.page")}</title>
  <meta name="help" content={li.get("help.about")} />
</svelte:head>

<div class="grid h-full w-full place-items-center">
  <p>{li.get("help.in_construction")}</p>
</div>
