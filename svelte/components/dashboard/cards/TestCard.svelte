<script>
  import CardRoot from "./components/CardRoot.svelte";

  export let cardStates;
  export let onDragEnd; // function
  export let onDragStart; // function
  let component = "TestCard";
</script>

<CardRoot {component} {cardStates} {onDragStart} {onDragEnd}>
  <h1 class="align-middle border-1 border-l-secondary-300 border-solid">
    Test card
  </h1>
</CardRoot>
