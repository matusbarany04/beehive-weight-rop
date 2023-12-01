<script>
  import { getLanguageInstance } from "../../../../components/language/languageRepository";

  export let handler;
  let rowsPerPage;

  rowsPerPage = handler.getRowsPerPage();

  const options = [5, 10, 20, 50, 100];

  const li = getLanguageInstance();

  /** @type String */
  let entries = li.get("beehives.entries");
  let parts = entries.split("%d");
</script>

<aside>
  {parts[0]}
  <select
    bind:value={$rowsPerPage}
    class="mb-2 mt-1 rounded-md border-2 border-slate-300 bg-white pl-4"
  >
    {#each options as option}
      <option value={option}>
        {option}
      </option>
    {/each}
  </select>

  {parts[1]}
</aside>

<style>
</style>
