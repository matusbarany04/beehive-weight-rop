<script>
  export let date;
  export let active;
  export let marked;
</script>

<div
  role="button"
  tabindex="0"
  on:mousedown
  class="bg-white p-2 shadow shadow-tertiary-300 duration-200 ease-in-out {marked
    ? 'z-0 scale-110 border-2 border-solid border-slate-400'
    : ''}"
>
  <div class={active ? "opacity-1" : "opacity-40"}>{date.getDate()}</div>
  <slot />
</div>
