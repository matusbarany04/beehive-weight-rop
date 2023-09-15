<script>
  import RouterLink from "../../components/RouterLink.svelte";
  import Background from "../app/components/auth/Background.svelte";

  const isValidEmail = (email) => {
    const EMAIL_REGEX =
      /[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?/;
    return EMAIL_REGEX.test(email.trim());
  };

  async function emailExist(email) {
    let response = await (await fetch("/emailExists/" + email)).json();
    return response["exists"];
  }

  $: fieldsError = {};
  let form = {};

  async function validateForm() {
    if (!isValidEmail(form.email)) {
      fieldsError.email = "Emailová adresa nie je platná !";
    } else if (await emailExist(form.email)) {
      fieldsError.email = "Účet s touto emailovou adresou už existuje";
    } else fieldsError.email = undefined;

    fieldsError.password =
      form.confirmPasswd !== form.password ? "Heslá sa nezhodujú !" : undefined;
  }
</script>

<div class="bg-primary v-screen relative h-screen bg-primary-500">
  <Background></Background>
  <div
    class="absolute right-0 flex h-screen w-full items-center p-1 sm:w-1/2 sm:min-w-[50rem] sm:justify-center"
  >
    <div
      class="box-border h-max w-full flex-col rounded-xl bg-tertiary-100 p-8 sm:w-9/12 sm:space-y-5"
    >
      <h1 class="header text-6xl font-bold">Registrovať sa.</h1>
      <br />

      <form class="flex-col space-y-4" action="/registerUser" method="POST">
        {#if fieldsError.email !== undefined}
          <div class="zero">
            <div class="popup">
              <span class="error">{fieldsError.email}</span>
            </div>
          </div>
        {/if}
        <div><label for="email">Email:</label></div>
        <input
          class="h-10 w-full rounded-md bg-white px-5 {fieldsError.email
            ? 'border-error border-2 border-solid'
            : ''}"
          type="email"
          name="email"
          placeholder="Email address"
          id="email"
          on:keydown={validateForm}
          on:focusout={validateForm}
          bind:value={form.email}
          required
        />

        <div><label for="name">First name:</label></div>
        <input
          class="h-10 w-full rounded-md bg-white px-5"
          type="text"
          name="name"
          placeholder="Name"
          id="name"
          required
        />

        <div><label for="password">Password:</label></div>
        <input
          class="h-10 w-full rounded-md bg-white px-5"
          type="password"
          name="password"
          placeholder="Password"
          id="password"
          bind:value={form.password}
          required
        />

        <div><label for="confirm_password">Potvrď heslo:</label></div>
        <input
          class="h-10 w-full rounded-md bg-white px-5"
          type="password"
          name="confirm_password"
          id="confirm_password"
          placeholder="Potvrď heslo"
          on:keydown={validateForm}
          bind:value={form.confirmPasswd}
          required
        />

        {#if fieldsError.password !== undefined}
          <div class="zero">
            <div class="popup">
              <span class="error">{fieldsError.password}</span>
            </div>
          </div>
        {/if}

        <div class="btn-container">
          <button
            disabled={fieldsError.password !== undefined &&
            fieldsError.email !== undefined
              ? "disabled"
              : ""}
            class="mt-8 w-full rounded-xl bg-secondary-400 p-2 text-xl font-bold duration-100 hover:scale-[1.01] active:scale-[.99]"
            >Registrovat sa
          </button>

          <p class="mt-1">
            Už máte účet? Prihlásiť sa môžete
            <RouterLink url="/login"
              ><span class="inline font-bold italic text-secondary-800"
                >tu
              </span>
            </RouterLink>
          </p>
        </div>
      </form>
    </div>
  </div>
</div>

<style lang="scss">
</style>
