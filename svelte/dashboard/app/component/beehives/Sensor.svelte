<script>
  export let name;
  export let img;
</script>

<div
  class="w-min cursor-pointer rounded bg-slate-100 p-2 text-center hover:scale-105"
>
  <img class="m-auto" src={img} alt={name} />
  <div>{name}</div>
</div>
