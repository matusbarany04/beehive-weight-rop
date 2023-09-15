<script>
  import CardRoot from "./components/CardRoot.svelte";

  export let cardStates;
  export let onDragEnd; // function
  export let onDragStart; // function
  let component = "TestCard";
</script>

<CardRoot {component} {cardStates} {onDragStart} {onDragEnd}>
  <h1 class="border-1 border-solid border-l-secondary-300 align-middle">
    Test card
  </h1>
</CardRoot>
