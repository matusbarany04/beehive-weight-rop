<script>
  let promise = getList();

  async function getList() {
    const res = await fetch(`/getUsers`);
    const text = await res.json();
    if (res.ok) {
      console.log(text)
      return text;
    } else {
      throw new Error(text);
    }
  }
</script>

{#await promise}
  <p>...waiting</p>
{:then response}
  {#each Object.values(response) as element, index}
    <p>{index}. The user:  {element.name}</p>
  {/each}

{:catch error}
  <p style="color: red">{error.message}</p>
{/await}
