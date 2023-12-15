<script>
  export let width = 48;
  export let height = 48;

  export  let flip = false
  export let className = "";
</script>
<div class="{className}">
  <img class="{flip ? '-scale-x-100' : ''}" src="/icons/bee.svg" width="{width}" height="{height}">

</div>
