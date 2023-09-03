<script>
  import {navigateWithPrefix} from "../route.serv";
  import 'style.css';
  export let type = "secondary";

  /**
   * @param {string} link optional, if specified button will redirect before action
   */
  export let link;
  /**
   * @param {string} image  optional, if specified image will be displayed left to the text, image path + name located in folder static
   */
  export let image;
  /**
   * @param {function} onClick function when the button is clicked
   */
  export let onClick = () => {
    console.log("Button action does not exist!");
  };
</script>

<!-- svelte-ignore a11y-click-events-have-key-events -->
<div
  on:click={() => {
    onClick();
    if (link) {
      navigateWithPrefix(link);
    }
  }}
  class={"h-8 w-8 aspect-square cursor-pointer flex items-center justify-center rounded-full duration-100 hover:scale-105 active:scale-95 " +
    type}
>
  {#if image}
    <div
      class="h-4 aspect-square bg-contain bg-no-repeat bg-[#000] bg-[var(--icon-color)] image"
      style="mask-image: url(/{image}); -webkit-mask-image: url(/{image}); "
    />
  {/if}
</div>

<style>
  .image {
    mask-size: 100%;
    -webkit-mask-size: 100%;
    mask-size: 100%;
  }
</style>
