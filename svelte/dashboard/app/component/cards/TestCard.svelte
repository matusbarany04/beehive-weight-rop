<script>
  import CardRoot from "./components/CardRoot.svelte";
  
  export let contentEditable = true;
  export let cardStates;

  let component = "TestCard";
</script>

<CardRoot
  contentEditable="{contentEditable}" {component} {cardStates}>
  <h1 class="border-1 border-solid border-l-secondary-300 align-middle">
    Test card
  </h1>
</CardRoot>
