<script>
  export let handler;
  let rowsPerPage;

  rowsPerPage = handler.getRowsPerPage();

  const options = [5, 10, 20, 50, 100];
</script>

<aside>
  Show
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
  entries
</aside>

<style>
</style>
