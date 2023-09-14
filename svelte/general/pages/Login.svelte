<script>
  import RouterLink from "../../components/RouterLink.svelte";
  const urlParams = new URLSearchParams(window.location.search);
  const invalid = urlParams.get("invalid");
</script>

<div class="bg-primary v-screen relative h-screen bg-primary-500">
  <div class="absolute h-screen w-full">
    <img alt="grass" class="absolute bottom-0" src="../img/grass.svg" />
    <img
      alt="beehive"
      class="absolute bottom-20 left-[10%] max-h-[80vh] w-1/3 p-5 align-bottom"
      src="../img/beehive2.svg"
    />
  </div>
  <div
    class="absolute right-0 flex h-screen w-1/2 min-w-[40rem] items-center justify-center"
  >
    <div
      class="box-border h-max w-9/12 flex-col space-y-5 rounded-xl bg-tertiary-100 p-8"
    >
      <h1 class="header text-6xl font-bold">Prihlásiť sa.</h1>
      <p class="alternative">
        Nemáte účet? Vytvoriť si ho môžete <RouterLink url="/register"
          >tu</RouterLink
        >
      </p>

      {#if invalid}
        <div class="text-error">Neplatný email alebo heslo !</div>
      {/if}

      <form class="flex-col space-y-4" action="/loginUser" method="POST">
        <div><label for="email">Email:</label></div>

        <input
          class="h-10 w-full rounded-md bg-white px-5"
          type="text"
          name="email"
          id="email"
          placeholder="Email"
          required
        />

        <div><label for="password">Heslo:</label></div>

        <input
          class="h-10 w-full rounded-md bg-white px-5"
          type="password"
          name="password"
          id="password"
          placeholder="Heslo"
          required
        />

        <button
          type="submit"
          class="mt-8 w-full rounded-xl bg-secondary-400 p-2 text-xl font-bold duration-100 hover:scale-[1.01] active:scale-[.99]"
          >Príhlásiť sa</button
        >
      </form>
    </div>
  </div>
</div>

<style lang="scss">
</style>
