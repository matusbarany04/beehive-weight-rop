<script>
  import RouterLink from "../../components/RouterLink.svelte";
  import Background from "../app/components/auth/Background.svelte";
  import ButtonSmall from "../../components/Buttons/ButtonSmall.svelte";
  import CircleButton from "../../components/Buttons/CircleButton.svelte";
  import Bee from "../../components/Bee.svelte";

  const urlParams = new URLSearchParams(window.location.search);
  const invalid = urlParams.get("invalid");
</script>

<div class="bg-primary v-screen relative flex h-screen flex-row bg-primary-500">
  <div class="absolute left-0 top-0 p-4">
    <CircleButton type="primary" link="/" image="icons/arrow-left.svg"
    ></CircleButton>
  </div>
  <!--  banner div-->
  <div class="sm:flex-1 shadow-2xl">
    <div
      class="h-full w-full bg-cover bg-right bg-no-repeat"
      style="background-image: url('/img/bg/scenery.jpeg')"
    ></div>
  </div>

  <!--  login form -->

  <div
    class="flex h-screen w-full max-w-full flex-1 items-center justify-center rounded-l-md bg-primary-100 shadow-2xl sm:max-w-sm md:max-w-md xl:max-w-xl"
  >

    <div
      class="box-border text-white h-max max-w-md flex-col rounded-3xl p-8 sm:space-y-5"
    >
      <h1 class="header text-6xl font-bold relative">Prihlásiť sa.

        <Bee className="absolute right-5 -top-12"></Bee>
      </h1>
      <p class="alternative ">
        Nemáte účet? Vytvoriť si ho môžete
        <RouterLink url="/register"
          ><span class="inline font-bold italic text-secondary-800">tu</span>
        </RouterLink>

    
      </p>

      {#if invalid}
        <div class="text-error text-secondary-400">Neplatný email alebo heslo !</div>
      {/if}

      <form class="flex-col space-y-4" action="/loginUser" method="POST">
        <div><label for="email">Email:</label></div>

        <input
          class="text-primary-100 h-10 w-full rounded-md bg-white px-5"
          type="text"
          name="email"
          id="email"
          placeholder="Email"
          required
        />

        <div><label for="password">Heslo:</label></div>

        <input
          class="text-primary-100 h-10 w-full rounded-md bg-white px-5"
          type="password"
          name="password"
          id="password"
          placeholder="Heslo"
          required
        />

        <button
          type="submit"
          class="mt-8 w-full rounded-xl bg-secondary-400 relative p-2 text-xl font-bold duration-100 hover:scale-[1.01] active:scale-[.99]"
          >Príhlásiť sa

          <Bee flip="{true}" className="absolute left-5 -bottom-16"></Bee>
        </button>
      </form>
    </div>
  </div>
</div>
