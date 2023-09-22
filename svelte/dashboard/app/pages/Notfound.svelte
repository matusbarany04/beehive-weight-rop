<script>
  /**
   * @fileoverview This page is a default route
   * @module NotFound
   */

  import message from "../stores/message";

  message.setMessage("");
</script>

<div class="flex flex-col items-center justify-center">
  <h1 class="my-4 text-center text-9xl font-bold sm:inline">404</h1>

  <h1 class="text-center text-base">
    Veľmi sa ospravedlňujeme, ale nenašli sme čo hľadáte :(
  </h1>
  <img src="/img/hypno_bee.png" class="aspect-auto w-16" />
</div>
