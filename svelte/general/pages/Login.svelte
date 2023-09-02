<script>
  
</script>

<div class="relative v-screen">
  <div class="background">
    <div class="grass" />
    <div class="beehive" />
  </div>
  <div class="absolute w-1/2 h-screen flex bg-slate-300">
    <div class="login shadow">
      <h1 class="header font-bold">Prihlásiť sa.</h1>
      <p class="alternative">
        Nemáte účet? Vytvoriť si ho môžete <a href="register"> tu </a>
      </p>

      <form class="submit" action="/loginUser" method="POST">

        <label for="email">Email:</label>

        <input class={"textInput "} type="text" name="email" placeholder="Email"/>

        <label for="password">Heslo:</label>

        <input
          class="textInput " type="password" name="password" placeholder="Heslo"/>

        <button type="submit" class="button submitButton">Príhlásiť sa</button>
      </form>
    </div>
  </div>
</div>

