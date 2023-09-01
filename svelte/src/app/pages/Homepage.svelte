<script>
  import GetList from '../component/GetList.svelte'
  
  
  let counter = 0;
  
  function click() {
    counter++;
  }
</script>

<h1>Home sweet Home</h1>



<button on:click={click}>Increase</button>
Number: {counter}

<GetList />

536894