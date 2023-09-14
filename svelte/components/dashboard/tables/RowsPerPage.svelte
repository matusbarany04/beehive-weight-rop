<script >
    export let handler
    let rowsPerPage;
    
    rowsPerPage = handler.getRowsPerPage();
    
    const options = [5, 10, 20, 50, 100]
</script>

<aside>
    Show
    <select bind:value={$rowsPerPage} class="pl-4 mt-1 mb-2 rounded-md border-2 bg-white border-slate-300">
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
