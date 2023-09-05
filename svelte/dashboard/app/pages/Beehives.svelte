<script>
    import DoubleGraph from "../../../../components/dashboard/cards/DoubleGraph.svelte";
    import LineGraph from "../../../../components/dashboard/cards/LineGraph.svelte";
    import Panel from "../../../../components/dashboard/panel/Panel.svelte";
    // import { DataHandler } from "@vincjo/datatables";

    import { someData } from "./data";
    import Button from "../../../../Buttons/Button.svelte";
    import CircleButton from "../../../../Buttons/CircleButton.svelte";

    import Modal from "../../../../components/Modal.svelte";
    import PercentageCard from "../../../../components/dashboard/cards/PercentageCard.svelte";
    import { onMount } from "svelte";
    import { jsonToCsv, triggerDownloadCsv } from "../../../components/lib/utils/static";

    $: ({ user, sessionid } = $page.data);
    import { dataHandler } from "../../../../components/dashboard/cards/dataHandler";
    import {
        DataHandler,
        Datatable,
        Th,
        ThFilter,
        RowCount,
        Pagination,
    } from "@vincjo/datatables";
    import Search from "../../../../components/dashboard/tables/Search.svelte";
    import RowsPerPage from "../../../../components/dashboard/tables/RowsPerPage.svelte";
    export let data;

    const handler = new DataHandler(dataHandler.dataToTableFormat(), {
        rowsPerPage: 10,
    });
    const rows = handler.getRows();

    import { page } from "$app/stores";
    import { message } from "$lib/utils/dashboard";
    import { fromJSON } from "postcss";

    onMount(() => {
        message.set("Zoznam Váh");
    });

    function exportData() {
        const jsonData = [];

        for (let i = 0; i < data.beehiveData.data.length; i++) {
            const row = data.beehiveData.data[i];
            if (row.statuses.length > 0) {
                jsonData.push({
                    name: row.name,
                    battery: row.statuses[0].battery,
                    status: row.statuses[0].status,
                    token: row.token,
                    weight: row.statuses[0].weight,
                });
            } else {
                jsonData.push({
                    name: row.name,
                    battery: "--",
                    status: "chyba",
                    token: row.token,
                    weight: "--",
                });
            }
        }

        const csvdata = jsonToCsv(jsonData);

        triggerDownloadCsv(csvdata, "table");
    }
    let showModal = false;
</script>

<svelte:head>
    <title>Úle</title>
    <meta name="Úle" content="Úle" />
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
                <Button
                        image="icon/export.svg"
                        text="Exportovať"
                        type="secondary"
                        onClick={exportData}
                />
                <Button
                        image="icon/add_thin.svg"
                        text="Pridať nový úľ"
                        type="primary"
                        link="/auth/dashboard/beehives/add"
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

            <RowsPerPage {handler} />
        </div>
        <div class="h-[1px] w-full bg-slate-200 mt-3" />
        <div class="flex-1 w-full overflow-x-scroll  max-h-[48rem] flex">
            <table class="flex-1">
                <thead class="bg-white">
                <tr class="h-12">
                    <th class="text-slate-500"></th>
                    <Th {handler} orderBy="name">Názov váhy</Th>
                    <Th {handler} orderBy="battery">Batéria</Th>
                    <Th {handler}>Status</Th>
                    <Th {handler} orderBy="timestamp">Dátum pridania</Th>
                    <Th {handler} orderBy="weight">Váha</Th>
                    <Th {handler}></Th>
                </tr>
                </thead>
                <tbody>
                {#each $rows as row, index}
                    <tr class="h-20">
                        <td class="">
                            <div
                                    class="bg-secondary-500 aspect-square rounded-full h-2 w-2"
                            />
                        </td>

                        <td class="font-normal">{row.name}</td>
                        <td> {-1 + index} % </td>
                        <td
                        ><div
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
                                {#if row.status}
                                    {row.status}
                                {:else}
                                    Chyba
                                {/if}
                            </p>
                        </div>
                        </td>
                        <td>
                            {new Date(row.timestamp).toLocaleString()}
                            <!-- {#if row.statuses.length > 0}
                            {new Date(row.statuses[0].timestamp).toLocaleString()}
                          {:else}
                            Nedostatok dát
                          {/if} -->
                        </td>
                        <td class="font-bold">
                            {#if row.weight}
                                {row.weight}kg
                            {:else}
                                Nedostatok dát
                            {/if}
                        </td>
                        <td
                        ><Button
                                type="secondary"
                                text="Detail"
                                link={`/auth/dashboard/beehives/${row.token}`}
                                onClick={() => {}}
                        />
                        </td>
                    </tr>
                {/each}
                </tbody>
            </table>
        </div>
        <footer class="flex flex-col items-center justify-between md:flex-row pt-4">
            <RowCount {handler} />
            <Pagination {handler} />
        </footer>
    </div>
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
