<script>
  export let size_px = 24;
  export let rotation = 0;
  export let className = "";
  export let colorClass = "text-white"; // Default color as a Tailwind CSS class
</script>

<!-- Set the background color of the div to transparent -->
<div
  class={`${className} bg-transparent`}
  style={`width: ${size_px}px; height: ${size_px}px; transform: rotate(${rotation}deg); transform-origin: center;`}
>
  <svg
    class="h-full w-full"
    width="100"
    height="100"
    viewBox="0 0 100 100"
    fill="none"
    xmlns="http://www.w3.org/2000/svg"
  >
    <path
      fill-rule="evenodd"
      clip-rule="evenodd"
      d="M100 100V0C100 55.2285 55.2285 100 0 100H100Z"
      class={`fill-current ${colorClass}`}
    />
    <!-- Apply the color class directly to the path -->
  </svg>
</div>
