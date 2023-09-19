<script>
  export let name;
  export let img;
</script>

<div class="rounded bg-slate-100 text-center w-min p-2 cursor-pointer hover:scale-105">
  <img class="m-auto" src={img} alt={name} />
  <div>{name}</div>
  
</div>
