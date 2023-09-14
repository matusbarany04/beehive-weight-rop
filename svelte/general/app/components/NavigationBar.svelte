<script>
  import Logo from "../../../components/Logo.svelte";
  import RouterLink from "../../../components/RouterLink.svelte";
</script>

<nav class="w-full flex flex-row h-12">
  <div class="w-24 flex items-center justify-center">logo</div>
  <div class="links flex-1 flex items-center gap-4 justify-center">
    <RouterLink url="/" class="px-2"><p>Domov</p></RouterLink>
    <RouterLink url="/" class="px-2"><p>Panel</p></RouterLink>
    <RouterLink url="/" class="px-2"><p>Kontakt</p></RouterLink>
    <RouterLink url="/" class="px-2"><p>Doprava</p></RouterLink>
    <RouterLink url="/" class="px-2"><p>O nás</p></RouterLink>
    <RouterLink url="/" class="px-2"><p>Dokumentacia</p></RouterLink>
  </div>
  <div class="w-24 flex justify-center items-center">
    <RouterLink url="/login">Prihlásiť sa</RouterLink>
  </div>
</nav>
