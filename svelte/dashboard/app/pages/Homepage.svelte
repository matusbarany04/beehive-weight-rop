<script>
  import GetList from '../component/GetList.svelte'
  import TestComponent from "../../../components/TestComponent.svelte";
  import {IconHeart} from '@tabler/icons-svelte';

  let counter = 0;

  function click() {
    counter++;
  }
</script>

<h1>Home sweet Homes</h1>


<div class="iconHeart">
  <h1>hello icon heart</h1>
</div>


<button on:click={click}>Increase</button>
Number: {counter}

<TestComponent></TestComponent>
<GetList/>

536894