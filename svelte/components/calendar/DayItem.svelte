<script>
    export let date;
    export let active;
    export let marked;

</script>
<div on:mousedown class="dayItem ease-in-out duration-200 bg-white p-2 {marked ? 'border-solid border-2 border-slate-400 scale-110' : ''}">
    <div class={active ? "opacity-1" : "opacity-40"}>{date.getDate()}</div>
</div>
