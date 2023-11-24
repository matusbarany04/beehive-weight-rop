<script>
  /**
   * @fileoverview This page is for resolving user related problem and providing explanations
   * @module Help
   */
  import HelpDropdown from "../../../components/help/HelpDropdown.svelte";
  import SettingsHeader from "../component/settings/SettingsHeader.svelte";

  import message from "../stores/message";

  message.setMessage("Pomoc");
</script>

<svelte:head>
  <title>Pomoc</title>
  <meta
    name="help"
    content="This page is for resolving user related problem and providing explanations"
  />
</svelte:head>

<SettingsHeader
  title="Často kladené otázky"
  description="Nemôžete nájsť odpoveď, ktorú hľadáte? Obráťte sa na náš zákaznícky servis."
/>

<div class="divide-neutral-200 mx-auto mt-8 w-full">
  <HelpDropdown
    question="Čo je BuzzyBees?"
    answer="BuzzyBees je inovatívne riešenie na monitorovanie úľov navrhnuté tak, aby poskytovalo včelárom dáta v reálnom čase o zdraví úľa, produkcii medu, raste plodu a ďalších."
  />
  <HelpDropdown
    question="Ako funguje BuzzyBees?"
    answer="Využívaním pokročilých senzorov a vlastných algoritmov BuzzyBees meria hmotnosť, teplotu a ďalšie kľúčové parametre vašich úľov a potom tieto informácie prenáša do prístupnej aplikácie alebo panela."
  />
  <HelpDropdown
    question="Prečo je monitorovanie úľa dôležité?"
    answer="Monitorovanie hmotnosti a podmienok úľa môže pomôcť pri určovaní úrovní produkcie medu, zdravia včiel, rastu plodu, potravinových rezerv a možných včelích rojov. BuzzyBees pomáha zabezpečiť prosperitu vašich včelích kolónií."
  />

  <HelpDropdown
    question="Čím sa BuzzyBees líši od iných monitorovacích nástrojov?"
    answer="BuzzyBees kombinuje starodávne včelárske múdrosti s najmodernejšou technológiou. Naša ľahko použiteľná aplikácia, presné senzory a jedinečné algoritmy nás odlišujú od tradičných monitorovacích riešení."
  />

  <HelpDropdown
    question="Sú moje dáta v bezpečí?"
    answer="Rozhodne. Prioritou je súkromie používateľa a bezpečnosť dát. Všetky prenášané dáta sú šifrované a nikdy nepredávame vaše dáta tretím stranám."
  />
</div>
