<script>
  import { getErrorMessageByType, getErrorTypes } from "./errors";

  /**
   * @param {String} error type of an error message from errors.js
   */
  export let error;
</script>

<div class="flex h-full w-full items-center justify-center">
  <h1 class="text-center text-error-400">{getErrorMessageByType(error)}</h1>
</div>
