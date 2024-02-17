<script>
  import NavigationBar from "../app/components/NavigationBar.svelte";
  import Root from "../layout/Root.svelte";
  import SettingsHeader from "../../dashboard/app/component/settings/SettingsHeader.svelte";
  import HelpDropdown from "../../components/help/HelpDropdown.svelte";
  import { getLanguageInstance } from "../../components/language/languageRepository";

  const LI = getLanguageInstance();
</script>

<Root title="FaQ">
  <div
    class="relative mx-auto box-border w-full p-4 text-primary-100 md:flex-row"
  >
    <div class="min-h-16 mx-auto mb-4 lg:w-5/6">
      <div>
        <h1 class="mb-4 text-2xl font-semibold text-tertiary-200">
          {LI.get("subpages.faq.title")}
        </h1>
        <p class="text-base font-normal text-tertiary-300">
          {LI.get("subpages.faq.description")}
        </p>
      </div>
    </div>

    <div class="mx-auto mt-8 w-full divide-neutral-200">
      {#each LI.get("subpages.faq.questions") as question}
        <HelpDropdown
          bgClass="bg-primary-100"
          question={question.question}
          answer={question.answer}
        />
      {/each}
    </div>
  </div>
</Root>
