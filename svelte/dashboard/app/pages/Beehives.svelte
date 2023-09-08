<script>
  import Button from "../../../components/Buttons/Button.svelte";
  import {onMount} from "svelte";
  import {jsonToCsv, triggerDownloadCsv} from "../../../components/lib/utils/static";

  import {
    DataHandler,
    Th,
    RowCount,
    Pagination,
  } from "@vincjo/datatables";
  import Search from "../../../components/dashboard/tables/Search.svelte";
  import RowsPerPage from "../../../components/dashboard/tables/RowsPerPage.svelte";

  import shared, {onLoad} from "../stores/shared";
  import Loading from "../../../components/pages/Loading.svelte";
  import RouterLink from "../../../components/RouterLink.svelte";

  export let data;

  const user = shared.getUser();
  let rows, handler, statuses;

  onLoad("beehives", beehives => {
      handler = new DataHandler(beehives, {
        rowsPerPage: 10,
      });
    rows = handler.getRows();
  });



  onLoad("statuses", data => {
    statuses = data;
  });
  
  
  let showModal = false;
</script>

<svelte:head>
  <title>Úle</title>
  <meta name="Úle" content="Úle"/>
</svelte:head>

<div class="w-full h-full box-border">
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
  <div class="w-full h-auto flex flex-col bg-white p-8 mt-4 rounded-lg">
    <div
      class="h-36 items-center flex flex-col gap-2 content-between md:flex-row md:gap-0 md:h-8"
    >
      <div class="flex-1 flex flex-col gap-4 md:flex-row items-center">
        <h1
          class="font-extrabold whitespace-nowrap text-ellipsis no_wrap text-2xl"
        >
          Moje úle
        </h1>
      </div>

      <div
        class="w-full items-center justify-center flex flex-1 gap-4 md:justify-end"
      >
        <RouterLink url="../../dashboardApi/downloadCSV" reload="true"> 
          <Button
            image="icons/export.svg"
            text="Exportovať"
            type="secondary"/>
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
      class="flex justify-between items-center md:h-8 mt-4 flex-col md:flex-row"
    >
      <Search
        class="flex-1 h-8 w-full max-w-sm  pl-4 mt-1 mb-2 rounded-md border-2 border-slate-300"
        {handler}
      />

      <RowsPerPage {handler}/>
    </div>
    <div class="h-[1px] w-full bg-slate-200 mt-3"></div>
    <div class="flex-1 w-full overflow-x-scroll  max-h-[48rem] flex">
      <table class="flex-1">
        <thead class="bg-white">
        <tr class="h-12">
          <th class="text-slate-500"></th>
          <Th {handler} orderBy="name">Názov váhy</Th>
          <Th {handler} orderBy="battery">Batéria</Th>
          <Th {handler}>Status</Th>
          <Th {handler} orderBy="timestamp">Posledná aktualizácia</Th>
          <Th {handler} orderBy="weight">Hmotnosť</Th>
          <Th {handler}/>
        </tr>
        </thead>
        <tbody>
        {#each $rows as row, index}
          <tr class="h-20">
            <td class="">
              <div
                class="bg-secondary-500 aspect-square rounded-full h-2 w-2"></div>
            </td>

            <td class="font-normal">{row.name}</td>
            <td>
              {#if statuses}
              {shared.getBattery(row.token)}%
              {:else}
                <img class="w-8 h-8" src="../../img/loading.gif" alt="loading...">
              {/if}
            </td>
            <td
            >
              <div
                class="h-8 w-20 px-1 box-content flex items-center justify-center rounded-full {row.status ===
                  'ok'
                    ? 'bg-confirm-200'
                    : 'bg-error-200'}"
              >
                <p
                  class="text-ellipsis no_wrap font-semibold whitespace-nowrap {row.status ===
                    'ok'
                      ? 'text-confirm-600'
                      : 'text-error-500'}"
                >
                  
                  {#if statuses}
                    {statuses[row.token]["currentStatus"]}
                  {:else}
                    <img class="w-8 h-8" src="../../img/loading.gif" alt="loading...">
                  {/if}
                </p>
              </div>
            </td>
            <td>
              {#if statuses}
                {new Date(shared.getLastUpdateTime(row.token)).toLocaleString()}
              {:else}
                <img class="w-8 h-8" src="../../img/loading.gif" alt="loading...">
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
                <img class="w-8 h-8" src="../../img/loading.gif" alt="loading...">
              {/if}
            </td>
            <td
            >
              <Button
                type="secondary"
                text="Detail"
                link={`/dashboard/beehives/${row.token}`}
                onClick={() => {}}
              />
            </td>
          </tr>
        {/each}
        </tbody>
      </table>
    </div>
    <footer class="flex flex-col items-center justify-between md:flex-row pt-4">
      <RowCount {handler}/>
      <Pagination {handler}/>
    </footer>
  </div>
    {:else}
      <Loading/>
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
