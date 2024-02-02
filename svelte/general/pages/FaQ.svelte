<script>
  import NavigationBar from "../app/components/NavigationBar.svelte";
  import Root from "../layout/Root.svelte";
  import SettingsHeader from "../../dashboard/app/component/settings/SettingsHeader.svelte";
  import HelpDropdown from "../../components/help/HelpDropdown.svelte";
</script>

<Root title="FaQ">
  <div
    class="relative mx-auto box-border w-full p-4 text-primary-100 md:flex-row"
  >
    <div class="min-h-16 mx-auto mb-4 lg:w-5/6">
      <div>
        <h1 class="mb-4 text-2xl font-semibold text-tertiary-200">
          Často kladené otázky
        </h1>
        <p class="text-base font-normal text-tertiary-300">
          Vitajte v našej zbierke často kladených otázok! Sme radi, že tu ste.
          Ak nám však niečo uniklo, neváhajte sa obrátiť na nás. Náš tím je tu
          pre vás s priateľskou pomocou a riešeniami na mieru. Vaša spokojnosť
          je pre nás prioritou, a sme tu, aby sme vám pomohli vytvoriť skvelý
          zážitok s našimi službami.
        </p>
      </div>
    </div>

    <div class="mx-auto mt-8 w-full divide-neutral-200">
      <HelpDropdown
        bgClass="bg-primary-100"
        question="Čo je BuzzyBees?"
        answer="BuzzyBees je inovatívne riešenie na monitorovanie úľov navrhnuté tak, aby poskytovalo včelárom dáta v reálnom čase o zdraví úľa, produkcii medu, raste plodu a ďalších."
      />
      <HelpDropdown
        bgClass="bg-primary-100"
        question="Ako funguje BuzzyBees?"
        answer="Využívaním pokročilých senzorov a vlastných algoritmov BuzzyBees meria hmotnosť, teplotu a ďalšie kľúčové parametre vašich úľov a potom tieto informácie prenáša do prístupnej aplikácie alebo panela."
      />
      <HelpDropdown
        bgClass="bg-primary-100"
        question="Prečo je monitorovanie úľa dôležité?"
        answer="Monitorovanie hmotnosti a podmienok úľa môže pomôcť pri určovaní úrovní produkcie medu, zdravia včiel, rastu plodu, potravinových rezerv a možných včelích rojov. BuzzyBees pomáha zabezpečiť prosperitu vašich včelích kolónií."
      />

      <HelpDropdown
        bgClass="bg-primary-100"
        question="Čím sa BuzzyBees líši od iných monitorovacích nástrojov?"
        answer="BuzzyBees kombinuje starodávne včelárske múdrosti s najmodernejšou technológiou. Naša ľahko použiteľná aplikácia, presné senzory a jedinečné algoritmy nás odlišujú od tradičných monitorovacích riešení."
      />

      <HelpDropdown
        bgClass="bg-primary-100"
        question="Sú moje dáta v bezpečí?"
        answer="Rozhodne. Prioritou je súkromie používateľa a bezpečnosť dát. Všetky prenášané dáta sú šifrované a nikdy nepredávame vaše dáta tretím stranám."
      />
    </div>
  </div>
</Root>
