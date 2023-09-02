<script>
import RouterLink from "../../components/RouterLink.svelte";

const isValidEmail = (email) => {
  const EMAIL_REGEX =
    /[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?/;
  return EMAIL_REGEX.test(email.trim());
};

async function emailExist(email) {
  let response = await (await (fetch("/emailExists/" + email))).json(); 
  return response["exists"];
}

$: fieldsError = {};
let form = {};

async function validateForm() {
  
  if(!isValidEmail(form.email)) {
    fieldsError.email = "Emailová adresa nie je platná !";
    
  } else if(await emailExist(form.email)) {
    fieldsError.email = "Účet s touto emailovou adresou už existuje";
  } else fieldsError.email = undefined;

  fieldsError.password = form.confirmPasswd !== form.password ? "Heslá sa nezhodujú !" : undefined;
}

function checkPassword() {
  let confirmPasswd = e.target.value + e.key;
  console.log(password, confirmPasswd);
  fieldsError.password = confirmPasswd !== password ? "Heslá sa nezhodujú !" : undefined;
}

function showError() {
  console.log("click");
}



</script>

<div class="relative bg-primary v-screen h-screen bg-primary-500">
    <div class="absolute w-full h-screen">
      <img class="absolute bottom-0" src="../img/grass.svg"/>
      <img class="absolute w-1/3 left-[10%] bottom-20 p-5 max-h-[80vh] align-bottom" src="../img/beehive2.svg"/>
    </div>
  <div class="absolute w-1/2 min-w-[50rem] h-screen flex right-0 justify-center items-center">
    <div class="p-8 rounded-xl bg-tertiary-100 box-border w-9/12 h-max">
      <h1 class="header font-bold text-6xl">Registrovať sa.</h1>
      <br/>

      <form class="flex-col space-y-4" action="/registerUser" method="POST" >
        {#if fieldsError.email !== undefined}
          <div class="zero">
            <div class="popup">
              <span class="error">{fieldsError.email}</span>
            </div>
          </div>
        {/if} 
        <div><label for="email">Email:</label></div>
          <input class="w-full h-10 rounded-md bg-white px-5 {fieldsError.email ? 'border-solid border-2 border-error' : ''}" 
                 type="email" name="email" placeholder="Email address" id="email" on:keydown={validateForm} on:focusout={validateForm} bind:value={form.email}required/>

        <div><label for="name">First name:</label></div>
        <input class="w-full h-10 rounded-md bg-white px-5" type="text" name="name" placeholder="Name" id="name" required/>

        <div><label for="password">Password:</label></div>
        <input class="w-full h-10 rounded-md bg-white px-5" 
               type="password" name="password" placeholder="Password" id="password" bind:value={form.password} required/>
            
        <div><label for="confirm_password">Potvrď heslo:</label></div>
        <input class="w-full h-10 rounded-md bg-white px-5" type="password" name="confirm_password"
               id="confirm_password" placeholder="Potvrď heslo" on:keydown={checkPassword} bind:value={form.confirmPasswd} required/>

        {#if fieldsError.password !== undefined}
          <div class="zero">
            <div class="popup">
              <span class="error">{fieldsError.password}</span>
            </div>
          </div>
        {/if}
        
        <div class="btn-container">
          <button disabled="{fieldsError.password !== undefined && fieldsError.email !== undefined ? 'disabled' : ''}"
                  class="w-full duration-100 mt-8 font-bold text-xl bg-secondary-400 p-2 rounded-xl hover:scale-[1.01] active:scale-[.99]"
          >Registrovat sa</button>

          <p class="mt-1">
            Už máte účet? Prihlásiť sa môžete <RouterLink url="/login">tu</RouterLink>
          </p>
        </div>
      </form>
    </div>
  </div>

</div>

<style lang="scss">
</style>
