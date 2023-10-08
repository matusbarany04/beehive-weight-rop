<script>
  /**
   * @fileoverview This is a button component for creating standardized buttons.
   * @module Button
   */

  import { navigateWithPrefix } from "../router/route.serv";

  /**
   * @param {string} text text of the button
   */
  export let text;
  /**
   * @param {string} type css type of the button from styles.css
   */
  export let type = "secondary";

  /**
   * @param {boolean} enabled enabled option
   */
  export let enabled = true;

  /**
   * @param {string} image  optional, if specified image will be displayed left to the text, image path + name located in folder static
   */
  export let image;

  /**
   * @param {string} image  optional, if specified image will be displayed left or right to the text, left is default
   * Type left, right
   */
  export let imagePosition = "left";

  /**
   * @param {string} link optional, if specified button will redirect before action
   */
  export let link;
  /**
   * @param {string} type button type when in forms
   */
  export let clickType = "none";
  /**
   * @param {string} formId to determine form
   */
  export let formId;
  /**
   * @param {function} onClick function when the button is clicked
   */
  export let onClick = () => {
    console.log("Button action does not exist!");
  };
</script>

<!-- svelte-ignore a11y-click-events-have-key-events -->
<button
  form={formId}
  type={clickType}
  on:click={() => {
    if (enabled) {
      onClick();
      if (link) {
        navigateWithPrefix(link);
      }
    }
  }}
  class="flex rounded-lg p-2 btn-{type}{enabled
    ? ''
    : '-disabled'} duration-200 hover:scale-[1.02]"
>
  {#if image && imagePosition === "left"}
    <div
      class="image mr-2 self-center"
      style="mask-image: url({image}); -webkit-mask-image: url(/{image}); "
    ></div>
  {/if}
  <p class="text no_wrap text-ellipsis whitespace-nowrap">
    {text}
  </p>
  {#if image && imagePosition === "right"}
    <div
      class="image ml-2 self-center"
      style="mask-image: url({image}); -webkit-mask-image: url(/{image}); "
    ></div>
  {/if}
</button>

<style>
  .image {
    mask-size: 100%;
    height: 20px;
    mask-repeat: no-repeat;
    mask-position: center;
    aspect-ratio: 1/1;
    background-size: contain;
    background-repeat: no-repeat;
    -webkit-mask-size: 100%;
    background-color: var(--icon-color, black);
  }

  .root {
    padding: 1.05rem 1rem;
    height: 1.5rem;
    border-radius: 10px;
    display: flex;
    width: min-content;
    align-items: center;
    justify-content: center;
    transition-duration: 100ms;
    cursor: pointer;
    gap: 0.5rem;
  }

  .text {
    color: var(--text-color, black);
  }
</style>
