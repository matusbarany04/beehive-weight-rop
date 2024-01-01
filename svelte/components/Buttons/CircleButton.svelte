<script>
  import { navigateWithPrefix } from "../router/route.serv";

  export let type = "secondary";

  /**
   * @param {string} link optional, if specified button will redirect before action
   */
  export let link;
  /**
   * @param {string} image  optional, if specified image will be displayed left to the text, image path + name located in folder static
   */
  export let image;

  
  export let className = ""
  /**
   * @param {function} onClick function when the button is clicked
   */
  export let onClick = () => {
    console.log("Button action does not exist!");
  };
</script>

<!-- svelte-ignore a11y-click-events-have-key-events -->
<button
  on:click={() => {
    onClick();
    if (link) {
      navigateWithPrefix(link);
    }
  }}
  class="{className} flex aspect-square h-8 w-8 cursor-pointer items-center justify-center rounded-full duration-100 hover:scale-105 active:scale-95 btn-{type}"
>
  {#if image}
    <div
      class="image aspect-square h-4 bg-[#000] bg-[var(--icon-color)] bg-contain bg-no-repeat"
      style="mask-image: url(/{image}); -webkit-mask-image: url(/{image}); "
    />
  {/if}
</button>

<style>
  .image {
    mask-size: 100%;
    -webkit-mask-size: 100%;
    mask-size: 100%;
  }
</style>
