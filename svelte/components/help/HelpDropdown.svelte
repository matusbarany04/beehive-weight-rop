<script>
  export let question = "";
  export let answer = "";
  export let className = "";
  export let bgClass = "bg-white";
</script>

<div
  class="min-h-16 mx-auto mb-4 rounded-lg {bgClass} p-4 lg:w-5/6 {className}"
>
  <details class="group">
    <summary
      class="flex cursor-pointer list-none items-center justify-between font-medium"
    >
      <span class="text-tertiary-100">{question}</span>
      <span class="transition group-open:rotate-180">
        <svg
          fill="white"
          height="24"
          shape-rendering="geometricPrecision"
          stroke="currentColor"
          stroke-linecap="round"
          stroke-linejoin="round"
          stroke-width="1.5"
          viewBox="0 0 24 24"
          width="24"
          ><path d="M6 9l6 6 6-6"></path>
        </svg>
      </span>
    </summary>
    <p class="group-open:animate-fadeIn mt-3 text-tertiary-200">
      {answer}
    </p>
  </details>
</div>
