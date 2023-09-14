<script>
  import RouterLink from "../../components/RouterLink.svelte";
</script>

<p>
  This link takes advantage of the javascript router and tries to find
  <code class="bg-slate-300">/dashboard</code> inside index spa, but unsuccessfully
</p>

<RouterLink url="/dashboard" baseRoute="true"
  >Dashboard Home trough RouterLink</RouterLink
>

<p>That's why we need to use a tag</p>
<a class="text-error-600 underline" href="/dashboard"
  >Dashboard Home trough anchor tag</a
>
