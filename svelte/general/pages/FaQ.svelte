<script>
  import NavigationBar from "../app/components/NavigationBar.svelte";
  import Root from "../layout/Root.svelte";
  import SettingsHeader from "../../dashboard/app/component/settings/SettingsHeader.svelte";
  import HelpDropdown from "../../components/help/HelpDropdown.svelte";
</script>

<Root>
  <div
    class="relative mx-auto mt-16 box-border max-w-7xl p-4 text-primary-100 text-white md:flex-row"
  >
    <SettingsHeader
      bgClass="bg-tertiary-100"
      title="Často kladené otázky"
      description="Nemôžete nájsť odpoveď, ktorú hľadáte? Obráťte sa na náš zákaznícky servis."
    />

    <div class="divide-neutral-200 mx-auto mt-8 w-full">
      <HelpDropdown
        bgClass="bg-tertiary-100"
        question="Čo je BuzzyBees?"
        answer="BuzzyBees je inovatívne riešenie na monitorovanie úľov navrhnuté tak, aby poskytovalo včelárom dáta v reálnom čase o zdraví úľa, produkcii medu, raste plodu a ďalších."
      />
      <HelpDropdown
        bgClass="bg-tertiary-100"
        question="Ako funguje BuzzyBees?"
        answer="Využívaním pokročilých senzorov a vlastných algoritmov BuzzyBees meria hmotnosť, teplotu a ďalšie kľúčové parametre vašich úľov a potom tieto informácie prenáša do prístupnej aplikácie alebo panela."
      />
      <HelpDropdown
        bgClass="bg-tertiary-100"
        question="Prečo je monitorovanie úľa dôležité?"
        answer="Monitorovanie hmotnosti a podmienok úľa môže pomôcť pri určovaní úrovní produkcie medu, zdravia včiel, rastu plodu, potravinových rezerv a možných včelích rojov. BuzzyBees pomáha zabezpečiť prosperitu vašich včelích kolónií."
      />

      <HelpDropdown
        bgClass="bg-tertiary-100"
        question="Čím sa BuzzyBees líši od iných monitorovacích nástrojov?"
        answer="BuzzyBees kombinuje starodávne včelárske múdrosti s najmodernejšou technológiou. Naša ľahko použiteľná aplikácia, presné senzory a jedinečné algoritmy nás odlišujú od tradičných monitorovacích riešení."
      />

      <HelpDropdown
        bgClass="bg-tertiary-100"
        question="Sú moje dáta v bezpečí?"
        answer="Rozhodne. Prioritou je súkromie používateľa a bezpečnosť dát. Všetky prenášané dáta sú šifrované a nikdy nepredávame vaše dáta tretím stranám."
      />
    </div>
  </div>
</Root>
