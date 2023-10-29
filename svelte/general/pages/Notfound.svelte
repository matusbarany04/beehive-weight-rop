<script>
  import Button from "../../components/Buttons/Button.svelte";
</script>

<div class="h-screen w-screen bg-slate-500">
  <div
    class="absolute h-full w-full bg-cover bg-no-repeat opacity-25"
    style:background-image="url('img/pattern_home.png')"
  ></div>
  <div
    class=" absolute flex h-full w-full flex-col items-center justify-center"
  >
    <img src="/img/sad_bee.png" class="aspect-auto max-w-md" />
    <h1 class="text-center text-xl font-bold">
      Veľmi sa ospravedlňujeme, ale nenašli sme čo hľadáte
    </h1>
    <Button
      image="icons/arrow-left.svg"
      className="mt-4"
      link="/"
      text="Naspäť domov"
      type="primary"
    ></Button>
  </div>
</div>
