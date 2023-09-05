<script>
    import CardRoot from "./components/CardRoot.svelte";

    export let cardStates;
    export let onDragEnd; // function
    export let onDragStart; // function
    let component = "DeletedCard"

</script>

<CardRoot {component} {cardStates} {onDragStart} {onDragEnd}>

    <h1 class="w-full text-center text-error-500 text-3xl">DELETED</h1>

</CardRoot>
