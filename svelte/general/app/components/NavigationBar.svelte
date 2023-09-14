<script>
  import RouterLink from "../../../components/RouterLink.svelte";
  import TW_BREAKPOINTS from "../../../components/lib/utils/static";

  let toggle = false;

  const resize = (e) => {
    if (window.innerWidth > TW_BREAKPOINTS.sm) {
      toggle = false;
    }
  };

  let pages = [
    { name: "Domov", link: "/", selected: true },
    { name: "Panel", link: "/panel", selected: false },
    { name: "Kontakt", link: "/kontakt", selected: false },
    { name: "Doprava", link: "/doprava", selected: false },
    { name: "O nás", link: "/o-nas", selected: false },
    { name: "Obchod", link: "/obchod", selected: false },
    { name: "Dokumentácia", link: "/dokumentacia", selected: false },
  ];
</script>

<svelte:window on:resize={resize} />

<nav class="bg-gray-800">
  <div class="mx-auto max-w-7xl px-2 sm:px-6 lg:px-8">
    <div class="relative flex h-16 items-center justify-between">
      <div class="absolute inset-y-0 left-0 flex items-center sm:hidden">
        <!-- Mobile menu button-->
        <button
          type="button"
          class="relative inline-flex items-center justify-center rounded-md p-2 text-gray-400 hover:bg-gray-700 hover:text-white focus:outline-none focus:ring-2 focus:ring-inset focus:ring-white"
          aria-controls="mobile-menu"
          aria-expanded="false"
          on:click={() => {
            toggle = !toggle;
          }}
        >
          <span class="absolute -inset-0.5"></span>
          <span class="sr-only">Open main menu</span>

          <svg
            class="block h-6 w-6"
            fill="none"
            viewBox="0 0 24 24"
            stroke-width="1.5"
            stroke="currentColor"
            aria-hidden={toggle}
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              d="M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5"
            />
          </svg>
        </button>
      </div>
      <div
        class="flex flex-1 items-center justify-center sm:items-stretch sm:justify-start"
      >
        <div class="flex flex-shrink-0 items-center">
          <img class="h-8 w-auto" src="/img/beeman.png" alt="BuzzyBees" />
        </div>
        <div class="hidden sm:ml-6 sm:block">
          <div class="flex flex-wrap space-x-4">
            <!-- Current: "bg-gray-900 text-white", Default: "text-gray-300 hover:bg-gray-700 hover:text-white" -->

            {#each pages as page}
              <a
                href={page.link}
                class="bg-gray-900 text-white rounded-md px-3 py-2 text-sm font-medium"
                >{page.name}</a
              >
            {/each}
          </div>
        </div>
      </div>
      <div
        class="absolute inset-y-0 right-0 flex items-center pr-2 sm:static sm:inset-auto sm:ml-6 sm:pr-0"
      >
        <button type="button" class="relative rounded-full bg-gray-800 p-1">
          <RouterLink url="/login"
            ><span
              class="text-gray-400 hover:text-white focus:outline-none focus:ring-2 focus:ring-white focus:ring-offset-2 focus:ring-offset-gray-800"
              >Prihlásiť sa</span
            >
          </RouterLink>
        </button>
      </div>
    </div>
  </div>

  <!-- Mobile menu, show/hide based on menu state. -->
  {#if toggle}
    <div class="absolute sm:hidden bg-primary-100 w-full" id="mobile-menu">
      <div class="space-y-1 px-2 pb-3 pt-2">
        {#each pages as page}
          <a
            href={page.link}
            class="text-gray-300 hover:bg-gray-700 hover:text-white block rounded-md px-3 py-2 text-base font-medium"
            >{page.name}</a
          >
        {/each}
      </div>
    </div>
  {/if}
</nav>
