<script>
    export let date;
    export let active;
    export let marked;

</script> ease-in-out duration-200  {marked ? 'border-solid border-2 border-slate-400 scale-110' : ''}">
    on:mousedown
    <div class={active ? "opacity-1" : "opacity-40"}>{date.getDate()}</div>

</div>

<style>
    .dayItem:hover {
        background-color: #ddd;
    }
</style>