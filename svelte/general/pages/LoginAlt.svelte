<script>
  import RouterLink from "../../components/RouterLink.svelte";
  import Background from "../app/components/auth/Background.svelte";
  import ButtonSmall from "../../components/Buttons/ButtonSmall.svelte";
  import CircleButton from "../../components/Buttons/CircleButton.svelte";
  import Bee from "../../components/Bee.svelte";
  import AuthLayout from "../app/components/auth/AuthLayout.svelte";

  const urlParams = new URLSearchParams(window.location.search);
  const invalid = urlParams.get("invalid");
</script>

<AuthLayout>
  <h1 class="header relative text-6xl font-bold">
    Prihlásiť sa.

    <Bee className="absolute right-5 -top-12"></Bee>
  </h1>
  <p class="alternative">
    Nemáte účet? Vytvoriť si ho môžete
    <RouterLink url="/register"
      ><span class="inline font-bold italic text-secondary-800">tu</span>
    </RouterLink>
  </p>

  {#if invalid}
    <div class="text-error text-secondary-400">
      Neplatný email alebo heslo !
    </div>
  {/if}

  <form class="flex-col space-y-4" action="/loginUser" method="POST">
    <div><label for="email">Email:</label></div>

    <input
      class="h-10 w-full rounded-md bg-white px-5 text-primary-100"
      type="text"
      name="email"
      id="email"
      placeholder="Email"
      required
    />

    <div><label for="password">Heslo:</label></div>

    <input
      class="h-10 w-full rounded-md bg-white px-5 text-primary-100"
      type="password"
      name="password"
      id="password"
      placeholder="Heslo"
      required
    />

    <button
      type="submit"
      class="relative mt-8 w-full rounded-xl bg-secondary-400 p-2 text-xl font-bold text-primary-600 duration-100 hover:scale-[1.01] active:scale-[.99]"
      >Príhlásiť sa

      <Bee flip={true} className="absolute left-5 -bottom-16"></Bee>
    </button>
  </form>
</AuthLayout>
