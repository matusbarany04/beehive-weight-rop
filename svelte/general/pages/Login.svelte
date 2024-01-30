<script>
  import RouterLink from "../../components/RouterLink.svelte";
  import Background from "../app/components/auth/Background.svelte";
  import ButtonSmall from "../../components/Buttons/ButtonSmall.svelte";
  import CircleButton from "../../components/Buttons/CircleButton.svelte";

  const urlParams = new URLSearchParams(window.location.search);
  const invalid = urlParams.get("invalid");
</script>

<div class="bg-primary v-screen relative h-screen bg-primary-500">
  <Background ></Background>
  <div class="absolute left-0 top-0 p-4">
    <CircleButton type="primary" link="/" image="icons/arrow-left.svg"
    ></CircleButton>
  </div>
  <div
    class="absolute right-0 flex h-screen w-full items-center p-1 sm:w-1/2 sm:min-w-[40rem] sm:justify-center"
  >
    <div
      class="box-border h-max w-full flex-col rounded-xl bg-tertiary-100 p-8 sm:w-9/12 sm:space-y-5"
    >
      <h1 class="header text-6xl font-bold">Prihlásiť sa.</h1>
      <p class="alternative">
        Nemáte účet? Vytvoriť si ho môžete
        <RouterLink url="/register"
          ><span class="inline font-bold italic text-secondary-800">tu</span>
        </RouterLink>
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
          >Príhlásiť sa
        </button>
      </form>
    </div>
  </div>
</div>

<style lang="scss">
</style>
