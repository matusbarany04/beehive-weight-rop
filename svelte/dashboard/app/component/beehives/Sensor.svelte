<script>
  export let name;
  export let img;
</script>

<div
  class="w-full cursor-pointer rounded bg-slate-100 p-2 text-center duration-200 hover:scale-110"
  on:click
>
  <img class="m-auto w-8" src={img} alt={name} />

  <div>{name}</div>
</div>
