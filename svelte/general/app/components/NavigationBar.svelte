<script>
  import RouterLink from "../../../components/RouterLink.svelte";
  import { TW_BREAKPOINTS } from "../../../components/lib/utils/staticFuncs";
  import { onMount } from "svelte";
  import {
    languages,
    getLanguageInstance,
  } from "../../../components/language/languageRepository";

  import { pushParam } from "../../../components/router/route.serv";
  import Button from "../../../components/Buttons/Button.svelte";
  import CircleButton from "../../../components/Buttons/CircleButton.svelte";

  let toggle = false;

  const resize = (e) => {
    if (window.innerWidth > TW_BREAKPOINTS.sm) {
      toggle = false;
    }
  };

  let pages = [
    // {name: "Dev Docs", link: "/test", selected: false},
    { name: "FaQ", link: "/faq", selected: false },
    { name: "Kontakt", link: "/contact", selected: false },
    { name: "O nás", link: "/about", selected: false },
    { name: "Obchod", link: "/shop", selected: false },
  ];

  let langDropdown = false;

  function changeLanguage(code) {
    langDropdown = false;
    pushParam("language", code);
    location.reload();
  }
</script>

<svelte:window on:resize={resize} />

<nav class="relative z-50 bg-primary-500">
  <div class="mx-auto max-w-7xl px-2 sm:px-6 lg:px-8">
    <div class="relative flex h-16 items-center justify-between">
      <div class="absolute inset-y-0 left-0 flex items-center sm:hidden">
        <!-- Mobile menu button-->
        <button
          type="button"
          class="relative inline-flex items-center justify-center rounded-md p-2 text-slate-300 hover:bg-primary-900 hover:text-white focus:text-slate-100 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-white"
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
          <a href="/">
            <img class="h-8 w-auto" src="/img/beeman.png" alt="BuzzyBees" />
          </a>
        </div>
        <div class="hidden sm:ml-6 sm:block">
          <div
            class="flex flex-wrap space-x-4 rounded-full bg-gradient-to-r from-white via-tertiary-200 via-80% to-gray-400 px-1 py-0.5 opacity-90 md:px-4"
          >
            <!-- Current: "bg-gray-900 text-white", Default: "text-gray-300 hover:bg-gray-700 hover:text-white" -->
            {#each pages as page}
              <a
                href={page.link}
                class="my-1 rounded-full px-3 py-0.5 text-sm font-normal text-primary-600 hover:bg-white"
                >{page.name}</a
              >
            {/each}
            <div class="relative">
              <button
                on:click={() => {
                  langDropdown = !langDropdown;
                }}
                class="my-1 rounded-full bg-primary-800 px-3 py-0.5 text-sm font-normal text-tertiary-100"
              >
                lan-{getLanguageInstance().getLanguage()}
              </button>

              {#if langDropdown}
                <div
                  class="animate-slide-in-top absolute left-0 z-20 mt-2 w-16 rounded-md bg-tertiary-100 text-sm text-primary-100 shadow-lg"
                >
                  <ul>
                    {#each Object.entries(languages) as [code, label]}
                      <li
                        on:click={() => {
                          changeLanguage(code);
                        }}
                        class="cursor-pointer rounded-md px-4 py-2 hover:bg-tertiary-400"
                      >
                        <p class="block text-center">
                          {label}
                        </p>
                      </li>
                    {/each}
                  </ul>
                </div>
              {/if}
            </div>
          </div>
        </div>
      </div>
      <div
        class="absolute inset-y-0 right-0 flex items-center pr-2 sm:static sm:inset-auto sm:ml-6 sm:pr-0"
      >
        <RouterLink url="/login">
          <Button
            slim={true}
            type="secondary-noborder"
            className="hidden md:block h-min w-min md:mr-8 shrink-0 grow-0 mx-auto"
            imagePosition="right"
            text={"Prihlásiť sa"}
          />

          <CircleButton
            type="secondary"
            className="block md:hidden hover:animate-wobble"
            image="icons/arrow-right.svg"
            imagePosition="right"
            text={"Prihlásiť sa"}
          ></CircleButton>
        </RouterLink>
      </div>
    </div>
  </div>

  <!-- Mobile menu, show/hide based on menu state. -->
  {#if toggle}
    <div class="absolute w-full bg-primary-500 sm:hidden" id="mobile-menu">
      <div class="z-10 space-y-1 px-2 pb-3 pt-2">
        {#each pages as page}
          <a
            href={page.link}
            class="block rounded-md px-3 py-2 text-base font-medium text-slate-300 hover:bg-gray-700 hover:text-white"
            >{page.name}</a
          >
        {/each}
      </div>
    </div>
  {/if}
</nav>
