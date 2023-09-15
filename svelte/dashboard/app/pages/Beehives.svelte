<script>
  import Button from "../../../components/Buttons/Button.svelte";

  import { DataHandler, Th, RowCount, Pagination } from "@vincjo/datatables";
  import Search from "../../../components/dashboard/tables/Search.svelte";
  import RowsPerPage from "../../../components/dashboard/tables/RowsPerPage.svelte";

  import shared, { onLoad } from "../stores/shared";
  import Loading from "../../../components/pages/Loading.svelte";
  import RouterLink from "../../../components/RouterLink.svelte";

  const user = shared.getUser();
  let rows, handler, statuses;

  onLoad("beehives", (beehives) => {
    handler = new DataHandler(beehives, {
      rowsPerPage: 10,
    });
    rows = handler.getRows();
  });

  onLoad("statuses", (data) => {
    statuses = data;
    /*handler =new DataHandler(statuses, {
     rowsPerPage: 10,
   });*/
  });

  let showModal = false;
</script>

<svelte:head>
  <title>Úle</title>
  <meta name="Úle" content="Úle" />
</svelte:head>

<div class="box-border h-full w-full">
  <!-- <div class="grid gap-4 grid-cols-1 md:grid-cols-4">
        <LineGraph
            cardStates={{
                id: "item.id",

                spanX: 1,
                spanY: 1,
                editing: false,
            }}
        />
        <PercentageCard
            cardStates={{
                id: "item.id",

                spanX: 1,
                spanY: 1,
                editing: false,
            }}
        />
        <LineGraph
            cardStates={{
                id: "item.id",

                spanX: 1,
                spanY: 1,
                editing: false,
            }}
        />
        <LineGraph
            cardStates={{
                id: "item.id",

                spanX: 1,
                spanY: 1,
                editing: false,
            }}
        />
    </div> -->

  <!-- <div class="cardImage" /> -->
  {#if handler && rows}
    <div class="mt-4 flex h-auto w-full flex-col rounded-lg bg-white p-8">
      <div
        class="flex h-36 flex-col content-between items-center gap-2 md:h-8 md:flex-row md:gap-0"
      >
        <div class="flex flex-1 flex-col items-center gap-4 md:flex-row">
          <h1
            class="no_wrap text-ellipsis whitespace-nowrap text-2xl font-extrabold"
          >
            Moje úle
          </h1>
        </div>

        <div
          class="flex w-full flex-1 items-center justify-center gap-4 md:justify-end"
        >
          <RouterLink url="../../dashboardApi/downloadCSV" reload="true">
            <Button
              image="icons/export.svg"
              text="Exportovať"
              type="secondary"
            />
          </RouterLink>

          <Button
            image="icons/add_thin.svg"
            text="Pridať nový úľ"
            type="primary"
            link="/beehives/add"
          />
        </div>
      </div>
      <div
        class="mt-4 flex flex-col items-center justify-between md:h-8 md:flex-row"
      >
        <Search
          class="mb-2 mt-1 h-8 w-full  max-w-sm flex-1 rounded-md border-2 border-slate-300 pl-4"
          {handler}
        />

        <RowsPerPage {handler} />
      </div>
      <div class="mt-3 h-[1px] w-full bg-slate-200"></div>
      <div class="flex max-h-[48rem] w-full flex-1 overflow-x-scroll">
        <table class="flex-1">
          <thead class="bg-white">
            <tr class="h-12">
              <th class="text-slate-500"></th>
              <Th {handler} orderBy="name">Názov váhy</Th>
              <Th {handler} orderBy="battery">Batéria</Th>
              <Th {handler}>Status</Th>
              <Th {handler} orderBy="timestamp">Posledná aktualizácia</Th>
              <Th {handler} orderBy="weight">Hmotnosť</Th>
              <Th {handler} />
            </tr>
          </thead>
          <tbody>
            {#each $rows as row, index}
              <tr class="h-20">
                <td class="">
                  <div
                    class="aspect-square h-2 w-2 rounded-full bg-secondary-500"
                  ></div>
                </td>

                <td class="font-normal">{row.name}</td>
                <td>
                  {#if statuses}
                    {shared.getBattery(row.token)}%
                  {:else}
                    <img
                      class="h-8 w-8"
                      src="../../img/loading.gif"
                      alt="loading..."
                    />
                  {/if}
                </td>
                <td>
                  {#if statuses}
                    <div
                      class="box-content flex h-8 w-20 items-center justify-center rounded-full px-1 {statuses[
                        row.token
                      ]['currentStatus'] === 'ok'
                        ? 'bg-confirm-200'
                        : 'bg-error-200'}"
                    >
                      <p
                        class="no_wrap text-ellipsis whitespace-nowrap font-semibold {statuses[
                          row.token
                        ]['currentStatus'] === 'ok'
                          ? 'text-confirm-600'
                          : 'text-error-500'}"
                      >
                        {statuses[row.token]["currentStatus"] === "ok"
                          ? "ok"
                          : "error"}
                      </p>
                    </div>
                  {:else}
                    <img
                      class="h-8 w-8"
                      src="../../img/loading.gif"
                      alt="loading..."
                    />
                  {/if}
                </td>
                <td>
                  {#if statuses}
                    {new Date(
                      shared.getLastUpdateTime(row.token),
                    ).toLocaleString()}
                  {:else}
                    <img
                      class="h-8 w-8"
                      src="../../img/loading.gif"
                      alt="loading..."
                    />
                  {/if}
                  <!-- {#if row.statuses.length > 0}
                {new Date(row.statuses[0].timestamp).toLocaleString()}
              {:else}
                Nedostatok dát
              {/if} -->
                </td>
                <td class="font-bold">
                  {#if statuses}
                    {#if shared.getWeight(row.token)}
                      {shared.getWeight(row.token)}kg
                    {:else}
                      Nedostatok dát
                    {/if}
                  {:else}
                    <img
                      class="h-8 w-8"
                      src="../../img/loading.gif"
                      alt="loading..."
                    />
                  {/if}
                </td>
                <td>
                  <Button
                    type="secondary"
                    text="Detail"
                    link={`/beehive/${row.token}`}
                    onClick={() => {}}
                  />
                </td>
              </tr>
            {/each}
          </tbody>
        </table>
      </div>
      <footer
        class="flex flex-col items-center justify-between pt-4 md:flex-row"
      >
        <RowCount {handler} />
        <Pagination {handler} />
      </footer>
    </div>
  {:else}
    <Loading />
  {/if}
</div>

<style lang="scss">
  td,
  th {
    padding: 4px 20px;
    border-bottom: 1px solid #dedede;
  }

  .cardImage {
    display: block;
    width: 100%;
    aspect-ratio: 16/9;
    background-image: url("/img/beelist.png");
    background-position: center;
    background-repeat: no-repeat;
    background-size: contain;
  }
</style>
