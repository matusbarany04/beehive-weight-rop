<script>
  import Button from "../../components/Buttons/Button.svelte";
</script>

<div class="bg-primary v-screen relative h-screen bg-primary-500">
  <div class="absolute h-screen w-full">
    <img alt="grass" class="absolute bottom-0" src="../img/grass.svg" />
    <img
      alt="beehive"
      class="absolute bottom-20 left-[10%] max-h-[80vh] w-1/3 p-5 align-bottom"
      src="../img/beehive2.svg"
    />
  </div>

  <div
    class="absolute right-0 flex h-screen w-full items-center justify-center md:w-1/2 md:min-w-[50rem]"
  >
    <div class="box-border h-max w-9/12 rounded-xl bg-tertiary-100 p-8">
      <h1 class="mb-2 text-xl font-bold">Váš účet bol úspešne vytvorený.</h1>
      <p>Na Vašu emailovú schránku sme odoslali potvrdzovací email.</p>
      <p>Túto kartu môžete zavrieť.</p>
      <br />
      <Button link="/login" type="primary" text="Prihlásiť sa"></Button>
    </div>
  </div>
</div>
