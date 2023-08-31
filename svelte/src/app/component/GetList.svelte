<script>
  let promise = getList();

  async function getList() {
    const res = await fetch(`/getUsers`);
    const text = await res.json();
    console.log(text)
    if (res.ok) {
      return text;
    } else {
      throw new Error(text);
    }
  }
</script>

{#await promise}
  <p>...waiting</p>
{:then response}
  {JSON.stringify(response)}
  {#each response as element, index}
    <p>{index}</p>
    <p>The number is {JSON.stringify( element)}</p>
  {/each}
  
{:catch error}
  <p style="color: red">{error.message}</p>
{/await}
