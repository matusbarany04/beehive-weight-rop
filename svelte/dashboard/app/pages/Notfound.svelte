<script>
  /**
   * @fileoverview This page is a default route
   * @module NotFound
   */

  import message from "../stores/message";
  import { getLanguageInstance } from "../../../components/language/languageRepository";

  message.setMessage("");

  var li = getLanguageInstance();
</script>

<div class="flex flex-col items-center justify-center">
  <!--  <h1 class="my-4 text-center text-9xl font-bold sm:inline">404</h1>-->

  <img src="/img/sad_bee.png" class="aspect-auto max-w-md" />
  <h1 class="text-center text-xl font-bold">
    {li.get("not_found.message")}
  </h1>
</div>
