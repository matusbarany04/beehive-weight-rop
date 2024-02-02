<script>
  import RouterLink from "../../components/RouterLink.svelte";
  import toast from "../../components/Toast/toast";
  import Button from "../../components/Buttons/Button.svelte";
  import ButtonSmall from "../../components/Buttons/ButtonSmall.svelte";
  import CircleButton from "../../components/Buttons/CircleButton.svelte";
  import ShaderCanvasTest from "../../components/test/ShaderCanvasTest.svelte";

  let scale = 1;

  let fragmentShader = `
		precision highp float;

		uniform vec2 u_resolution;
		uniform vec2 u_mouse;
		uniform float u_time;

		void main() {
			gl_FragColor = vec4(fract((gl_FragCoord.xy - u_mouse) / u_resolution), fract(u_time), 1);
		}
	`;

  import { derived } from "svelte/store";

  let cardList = [];

  const cardListLength = derived(cardList, ($cardList) => $cardList.length);

  function addCard() {
    cardList = [
      ...cardList,
      {
        /* your card properties */
      },
    ];
  }
</script>

<main>
  <p>Card List Length: {$cardListLength}</p>
  <button on:click={addCard}>Add Card</button>
</main>
<section class="grid h-screen w-screen place-items-center">
  <button class="h-8 w-24 rounded-xl btn-swipe-primary"> ahoj </button>
</section>
<section class="h-screen w-screen bg-tertiary-200">
  <ShaderCanvasTest {scale} {fragmentShader} />
</section>

<section class="h-screen min-h-[40rem] w-full bg-tertiary-200">
  <div class="scroll-animated-element bg-red h-64 w-64"></div>
</section>

<div style="height: 100vh; background-color: #f0f0f0;"></div>

<br class="h-10" />

<main class="min-h-screen w-full flex-1 bg-primary-200 pt-10">
  <div class="absolute left-0 top-0 p-4">
    <CircleButton type="primary" link="/" image="icons/arrow-left.svg"
    ></CircleButton>
  </div>
  <h1 class="text-center text-5xl font-bold text-tertiary-100">
    Router Link Docs
  </h1>
  <section class="mx-auto my-4 w-11/12 rounded-lg bg-tertiary-100 p-4">
    <h1 class="my-4 text-4xl font-bold">Router Link</h1>
    <p>
      This link takes advantage of the javascript router and tries to find
      <code class="bg-slate-300">/dashboard</code> inside index spa, but unsuccessfully
    </p>

    <RouterLink url="/dashboard" baseRoute="true"
      >Dashboard Home trough RouterLink
    </RouterLink>

    <p>That's why we need to use a tag</p>
    <a class="text-error-600 underline" href="/dashboard"
      >Dashboard Home trough anchor tag</a
    >

    <pre
      class="my-4 overflow-x-scroll rounded-md bg-slate-300 outline outline-1 outline-primary-100">
    <code class="text-black">
  {`  <RouterLink url="/dashboard" baseRoute="true"
      >Dashboard Home trough RouterLink
    </RouterLink>

    <p>That's why we need to use a tag</p>
    <a class="text-error-600 underline" href="/dashboard"
      >Dashboard Home trough anchor tag</a>`}
    </code>
  </pre>
  </section>

  <section class="mx-auto my-4 w-11/12 rounded-lg bg-tertiary-100 p-4">
    <h1 class="my-4 text-4xl font-bold">Toasts</h1>
    <div class="flex flex-col gap-4 md:flex-row">
      <Button onClick={() => toast.push("Meh. Meh.")} text="default"></Button>
      <Button
        onClick={() => toast.push("Be excited!", "special")}
        text="special"
      ></Button>
      <Button
        onClick={() => toast.push("Something went wrong 🐝", "error")}
        text="error"
      ></Button>
      <Button
        onClick={() => toast.push("It really happened!", "confirm")}
        text="confirm"
      ></Button>
    </div>

    <pre
      class="my-4 overflow-x-scroll rounded-md bg-slate-300 outline outline-1 outline-primary-100">
    <code class="text-black">
  {`
  <Button onClick={()=>toast.push("Meh. Meh.")} text="default"></Button>
  <Button onClick={()=>toast.push("Be excited!", "special")} text="special"></Button>
  <Button onClick={()=>toast.push("Something went wrong 🐝", "error")} text="error"></Button>
  <Button onClick={()=>toast.push("It really happened!", "confirm")} text="confirm"></Button>`}
    </code>
  </pre>
  </section>
</main>

<style>
  /* App.css */
  body {
    height: 200vh; /* Make the body taller for scrolling */
    margin: 0;
    font-family: Arial, sans-serif;
  }

  .scroll-animated-element {
    width: 100%;
    height: 300px;
    background-color: lightblue;
    margin-top: 50vh; /* Start the element below the viewport */
    transition: background-color 0.5s ease;
  }

  /* Define a scroll timeline named "color-change-timeline" */
  @scroll-timeline color-change-timeline {
    scroll-timeline-axis: vertical;
  }

  /* Keyframes for the color change animation */
  @keyframes colorChange {
    from {
      background-color: lightblue;
    }
    to {
      background-color: coral;
    }
  }

  /* Use the scroll timeline in the animation */
  .scroll-animated-element {
    animation: colorChange 2s ease infinite;
    animation-timeline: color-change-timeline;
    animation-range: 0% 100%; /* Animation runs from 0% to 100% of the scroll timeline */
  }
</style>
