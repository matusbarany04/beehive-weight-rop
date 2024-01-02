<script>
  import { onMount } from "svelte";
  import { createEventDispatcher } from "svelte";

  export let src;
  export let size = 5;
  export let enabled = true;

  const dispatch = createEventDispatcher();

  onMount(() => {
    const inspectorToggle = (value) => {
      dispatch("toggle", { value });
    };

    inspectorToggle(enabled);

    return () => {
      inspectorToggle(false);
    };
  });
</script>

<svg>
  <defs>
    <filter id="pixelate" x="0" y="0">
      <feFlood x="4" y="4" height="1" width="1" />
      <feComposite width={size * 2} height={size * 2} />
      <feTile result="a" />
      <feComposite in="SourceGraphic" in2="a" operator="in" />
      <feMorphology operator="dilate" radius={size} />
    </filter>
  </defs>

  <image
    width="100%"
    height="100%"
    preserveAspectRatio="xMidYMid slice"
    filter={enabled ? "url(#pixelate)" : ""}
    href={src}
  />
</svg>

<style>
  svg {
    width: 100%;
    height: 500px;
  }
</style>
