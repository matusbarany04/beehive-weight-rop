<script>
  
</script>

<div class="relative bg-primary v-screen h-screen bg-[black]">
  <div class="absolute w-full h-screen">
    <img class="absolute bottom-0" src="../img/grass.svg"/>
    <img class="absolute w-1/3 left-[10%] bottom-20 p-5 max-h-[80vh] align-bottom" src="../img/beehive2.svg"/>
  </div>
  <div class="absolute w-1/2 min-w-[40rem] h-screen flex right-0 justify-center items-center">
    <div class="p-8 rounded-xl bg-white box-border w-9/12 h-max flex-col space-y-5">
      <h1 class="header font-bold text-6xl">Prihlásiť sa.</h1>
      <p class="alternative">
        Nemáte účet? Vytvoriť si ho môžete <a href="register"> tu </a>
      </p>

      <form class="flex-col space-y-4" action="/loginUser" method="POST">

        <div><label for="email">Email:</label></div>

        <input class="w-full h-10 rounded-md bg-white px-5" type="text" name="email" id="email" placeholder="Email"/>

        <div><label for="password">Heslo:</label></div>

        <input class="w-full h-10 rounded-md bg-white px-5" type="password" name="password" id="password" placeholder="Heslo"/>

        <button type="submit" class="w-full duration-100 mt-8 font-bold text-xl bg-secondary-400 p-2 rounded-xl hover:scale-[1.01] active:scale-[.99]">Príhlásiť sa</button>
      </form>
    </div>
  </div>
</div>

<style lang="scss">
  @import 'tailwindcss/base';
  @import 'tailwindcss/components';
  @import 'tailwindcss/utilities';
</style>
