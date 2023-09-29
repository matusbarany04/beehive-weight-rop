<script>
  /**
   * @file-overview This page provides one of the options to add new beehive with token
   * @module BeehiveToken
   */
  import Button from "../../../../components/Buttons/Button.svelte";
  import Loading from "../../../../components/pages/Loading.svelte";
  import { navigate } from "../../../../components/router/route.serv";
  import message from "../../stores/message";
  import shared from "../../stores/shared";

  message.setMessage("Pridať úľ");

  const urlParams = new URLSearchParams(window.location.search);
  const token = urlParams.get("token");

  let timeout;

  if (token) {
    fetch("/dashboardApi/newPairing", { method: "POST", body: token })
      .then((r) => r.json())
      .then((status) => {
        if (status["status"] === "ok") {
          const interval = setInterval(async () => {
            const response = await fetch(
              "/dashboardApi/checkPairingStatus?token=" + token,
            );
            status = await response.json();
            console.log(status["status"]);
            if (status["status"] === "PAIRED") {
              location.href = "/dashboard/beehives/" + token + "/edit";
              clearInterval(interval);
            } else if (status["status"] === "TIMEOUT") {
              timeout = true;
              clearInterval(interval);
            }
          }, 1000);
        }
      });
  }
</script>

<div
  class="mb-5 rounded-lg bg-white p-5 md:border-0 md:border-r-2 md:border-slate-300 md:pr-2"
>
  <h1 class="text-4xl">Pridať nové zariadenie</h1>
</div>

<div class="{token ? '' : 'flex'} rounded-lg bg-white p-5">
  {#if !token}
    <div class="w-full p-10">
      Tu zadajte identifikačné číslo váhy (uvedené na obale)
      <p>
        <i
          >Upozornenie: SIM karta v úli musí mať PIN 0000 alebo žiadny PIN inak
          sa nedokáže pripojiť na internet</i
        >
      </p>

      <form class="justify-center text-center">
        <input
          class="m-4 w-full rounded-md border-2 border-slate-300 p-2 pl-4"
          name="token"
          placeholder="XXXXXXXXXXXXXXX"
        />
        <div class="flex w-full justify-center">
          <Button type="confirm" text="Potvrdiť" />
        </div>
      </form>
    </div>

    <div class="w-3/4 p-5">
      <img src="/img/beehive_token.svg" alt="ukazka vahy" />
    </div>
  {:else if timeout}
    <div>
      Nepodarilo sa nájsť váhu. Skontrolujte prosím, či sa váha dokáže pripojiť
      k internetu a skúste to znova.
    </div>
  {:else}
    <p>Hľadanie váhy...</p>
    <Loading />
    <div class="flex items-center">
      <img class="h-20" src="/img/press_button.svg" alt="stlacte tlacidlo" />
      Stlačte prosím tlačidlo REQ_CONNECT na váhe
    </div>
  {/if}
</div>
