<script>
  import Root from "../layout/Root.svelte";
  import AboutCard from "../app/components/about/AboutCard.svelte";
</script>

<Root
  title="O nás">
  <AboutCard
    name="Martin"
    description="Lorem ipsum dolor sit amet. Et vitae autem qui distinctio excepturi ut ratione impedit in sunt facilis aut eligendi consectetur. In architecto itaque et delectus sequi ut quia voluptate aut nobis iure."
    direction="left"
    image="/img/authors/martin.jpg"
  />

  <AboutCard
    name="Matúš"
    description="Quo quod dolorum ea eaque veritatis aut fugit beatae eos nobis enim aut consequatur voluptatem rem blanditiis repellat ut necessitatibus facere. Hic totam libero quo dolor distinctio qui iure omnis."
    direction="right"
    image="/img/authors/matus.jpg"
  />
</Root>
