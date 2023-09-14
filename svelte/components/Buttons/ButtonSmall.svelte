<script>
  import {navigateWithPrefix} from "../route.serv";
  import 'style.css';

  /**
   * @param {string} text text of the button
   */
  export let text;
  /**
   * @param {string} type css type of the button from styles.css
   */
  export let type = "secondary";
  /**
   * @param {string} image  optional, if specified image will be displayed left to the text, image path + name located in folder static
   */
  export let image;
  /**
   * @param {string} link optional, if specified button will redirect before action
   */
  export let link;
  /**
   * @param {function} onClick function when the button is clicked
   */
  export let onClick = () => {
    console.log("Button action does not exist!");
  };
</script>


<button
  class="root w-min flex rounded-lg px-2 h-min btn-{type}"
  on:click={() => {
    onClick();
    if (link) {
      navigateWithPrefix(link);
    }
  }}
>
  {#if image}
    <div
      class="image mr-1.5"
      style="mask-image: url(/{image}); -webkit-mask-image: url(/{image}); "
    />
  {/if}

  <p class="text text-ellipsis no_wrap whitespace-nowrap text-sm">
    {text}
  </p>
</button>

<style>
  .image {
    mask-size: 100%;
    height: 20px;
    aspect-ratio: 1/1;
    background-size: contain;
    mask-repeat: no-repeat;
    mask-position: center;
    background-repeat: no-repeat;
    -webkit-mask-size: 100%;
    background-color: var(--icon-color, black);
  }


  .text {
    color: var(--icon-color, black);
  }

  .root:hover {
    transform: scale(1.02);
  }

  .root:active {
    transform: scale(0.98);
  }
</style>
