<script>
  /**
   * @fileoverview This page shows a list of all beekepers beehives
   * @module Beehives
   */

  import Button from "../../../components/Buttons/Button.svelte";

  import { DataHandler, Th, RowCount, Pagination } from "@vincjo/datatables";
  import Search from "../component/tables/Search.svelte";
  import RowsPerPage from "../component/tables/RowsPerPage.svelte";

  import shared, { onLoad } from "../stores/shared";
  import Loading from "../../../components/pages/Loading.svelte";
  import RouterLink from "../../../components/RouterLink.svelte";
  import message from "../stores/message";
  import { navigate, route } from "../../../components/router/route.serv";
  import { onMount, tick } from "svelte";
  import { getLanguageInstance } from "../../../components/language/languageRepository";
  import sf from "../../../components/lib/utils/staticFuncs";

  const li = getLanguageInstance();

  const user = shared.getUser();
  let rows, handler, statuses;

  onLoad(["statuses", "beehives", "user"], (_bee, status_data) => {
    tick().then(() => {
      let beehives = Object.values(shared.getBeehives());
      let tableData = [];

      statuses = shared.getBeehives();

      for (/** @type {BeehiveObj} */ const beehive of beehives) {
        tableData.push({
          state: beehive.state,
          stateColor: beehive.getColorByState(),
          name: beehive.name,
          battery: beehive.getLastDataByType("battery"),
          status: beehive.getCurrentStatus(),
          weight: beehive.getLastDataByType("weight"),
          timestamp: beehive.getLastUpdateTime(),
          beehive_id: beehive.beehive_id,
        });
      }

      handler = new DataHandler(tableData, {
        rowsPerPage: 10,
      });
      rows = handler.getRows();
    });
  });

  let showModal = false;

  let exportPopup = false;

  message.setMessage(li.get("beehives.page_title"));
</script>

<svelte:head>
  <title>{li.get("beehives.page_title")}</title>
  <meta
    name={li.get("beehives.page_title")}
    content={li.get("beehives.page_title")}
  />
</svelte:head>

<div class="box-border h-full w-full">
  {#if handler && rows}
    <div
      class="mt-4 flex h-auto w-full flex-col rounded-lg bg-white px-0 py-8 shadow shadow-tertiary-300 sm:px-4 md:px-8"
    >
      <div
        class="flex h-36 flex-col content-between items-center gap-2 md:h-8 md:flex-row md:gap-0"
      >
        <div class="flex flex-1 flex-col items-center gap-4 md:flex-row">
          <h1
            class="no_wrap text-ellipsis whitespace-nowrap text-2xl font-extrabold"
          >
            {li.get("beehives.table_title")}
          </h1>
        </div>

        <div
          class="flex w-full flex-1 items-center justify-center gap-4 md:justify-end"
        >
          <div class="relative">
            <Button
              image="icons/export.svg"
              text={li.get("beehives.btn_export")}
              type="secondary"
              onClick={() => {
                exportPopup = !exportPopup;
              }}
            />
            {#if exportPopup}
              <div
                class="min-h-24 absolute top-12 flex w-32 flex-col gap-2 rounded-md border-2 border-slate-300 bg-white"
              >
                <RouterLink
                  baseRoute="true"
                  url="/dashboardApi/downloadCSV"
                  reload="true"
                >
                  <Button type="transparent" text={li.get("beehives.as_csv")}
                  ></Button>
                </RouterLink>
                <div class="border border-slate-300"></div>
                <RouterLink
                  baseRoute="true"
                  url="/dashboardApi/downloadExcel"
                  reload="true"
                >
                  <Button type="transparent" text={li.get("beehives.as_excel")}
                  ></Button>
                </RouterLink>
              </div>
            {/if}
          </div>

          <Button
            image="icons/add_thin.svg"
            text={li.get("beehives.btn_new")}
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
              <Th {handler} orderBy="name"
                >{li.get("beehives.table.beehive_name")}</Th
              >
              <Th {handler} orderBy="battery"
                >{li.get("beehives.table.battery")}</Th
              >
              <Th {handler}>{li.get("beehives.table.status")}</Th>
              <Th {handler} orderBy="timestamp"
                >{li.get("beehives.table.timestamp")}</Th
              >
              <Th {handler} orderBy="weight"
                >{li.get("beehives.table.weight")}</Th
              >
              <Th {handler} />
            </tr>
          </thead>
          <tbody>
            {#each $rows as row, index}
              <tr class="h-20">
                <td class="">
                  <div
                    class="aspect-square h-2 w-2 rounded-full bg-{row.stateColor}"
                  ></div>
                </td>

                <td class="font-normal">{row.name}</td>
                <td>
                  {#if statuses}
                    {row.battery}%
                  {:else}
                    <img
                      class="h-8 w-8"
                      src="../../img/loading.gif"
                      alt="loading..."
                    />
                  {/if}
                </td>
                <td>
                  {#if row}
                    <div
                      class="box-content flex h-8 w-20 items-center justify-center rounded-full px-1 {row.status ===
                      'ok'
                        ? 'bg-confirm-200'
                        : 'bg-error-200'}"
                    >
                      <p
                        class="no_wrap text-ellipsis whitespace-nowrap font-semibold {row.status ===
                        'ok'
                          ? 'text-confirm-600'
                          : 'text-error-500'}"
                      >
                        {row.status}
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
                    {row.timestamp}
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
                    {#if row.weight}
                      {row.weight}kg
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
                    text={li.get("beehives.btn_detail")}
                    link={`/beehives/${row.beehive_id}`}
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

<style>
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
