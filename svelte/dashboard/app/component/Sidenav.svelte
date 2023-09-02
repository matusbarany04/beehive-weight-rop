<script>
  import RouterLink from '../../../components/RouterLink.svelte'
</script>



<nav class="">
  <h1>Sidenav</h1>
  <ul >
    <li class="ml-4">
      <RouterLink url=''>Homepage</RouterLink>
    </li>
    <li class="ml-4">
      <RouterLink url='/help'>Help page</RouterLink>
    </li>
    <li class="ml-4">
      <RouterLink url='/asas'>WrongPage</RouterLink>
    </li>
  </ul>
</nav>