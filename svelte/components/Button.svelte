<script>
  import RouterLink from "./RouterLink.svelte";

  import "./styles.css";
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
   * @param {string} type button type when in forms
   */
  export let clickType = "none";
  /**
   * @param {function} onClick function when the button is clicked
   */
  export let onClick = () => {
    console.log("Button action does not exist!");
  };
</script>

<!-- svelte-ignore a11y-click-events-have-key-events -->
  <button
  type={clickType}
  on:click={() => {
    onClick();
    if (link) {
      goto(link);
    }
  }}
  class={"root " + type}
>
  {#if image}
    <div
      class="image"
      style="mask-image: url(/{image}); -webkit-mask-image: url(/{image}); "
    />
  {/if}
  <p class="text text-ellipsis no_wrap whitespace-nowrap">
    {text}
  </p>

<RouterLink>
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
    mask-size: 100%;
    /* background: black; */
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
    /* background-color: var(--color-secondary); */
  }

  .text {
    /* font-size: 1rem; */
    /* padding: 0; */
    /* margin: 0; */
  }
  .root:hover {
    transform: scale(1.02);
  }
  .root:active {
    transform: scale(0.98);
  }
</style>
