<script>
  import RouterLink from "../../components/RouterLink.svelte";
  import toast from "../../components/Toast/toast";
  import Button from "../../components/Buttons/Button.svelte";
</script>

<main class="min-h-screen w-full flex-1 bg-slate-200 pt-10">
  <section class="mx-auto my-4 w-11/12 rounded-lg bg-white p-4">
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

  <section class="mx-auto my-4 w-11/12 rounded-lg bg-white p-4">
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
