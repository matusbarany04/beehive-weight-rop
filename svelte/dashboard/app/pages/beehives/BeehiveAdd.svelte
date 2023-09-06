<script>
    import { page } from "$app/stores";

    // $: ({ user, sessionid } = $page.data);

    // import { message } from "$lib/utils/dashboard";
    import { onMount } from "svelte";
    import Button from "../../../../components/Buttons/Button.svelte";

    onMount(async () => {
        // message.set("Pridať váhu");
    });
</script>

<svelte:head>
    <title>Úle</title>
    <meta name="Pridať úl" content="Úle" />
</svelte:head>
<div
        class="flex-col mb-4 p-10 mx-auto flex justify-between bg-white gap-4 rounded-lg lg:w-5/6 lg:flex-row lg:justify-center"
>
    <div class="flex-1 md:pr-2 md:border-0 md:border-r-2 md:border-slate-300">
        <h1 class="text-4xl">Pridať nové zariadenie</h1>
        <p>
            Registrujte Vaše zariadenie do platformy BuzzyBees s následovanými údajmi.
            Identifikačný kód zariadenia nájdete na obale.
        </p>
    </div>

    <div class="flex-1 flex items-center md:justify-center">
        <form method="POST" class="flex-1 flex flex-col md:items-center">
            <div>
                <label for="name" class="">Názov</label> <br />
                <input
                        type="text"
                        name="name"
                        class="h-8 w-full sm:w-96 pl-4 mt-1 mb-2  rounded-md border-2 border-slate-300"
                />
            </div>
            <br />
            <div>
                <label for="beehive_id">Identifikačné čislo váhy</label> <br />
                <input
                        type="text"
                        name="beehive_id"
                        class="h-8 w-full sm:w-96 pl-4 mt-1 mb-2 rounded-md border-2 border-slate-300"
                />
            </div>
            <br />
            <div>
                <label for="interval">Interval merania</label> <br />
                <input
                        type="text"
                        name="interval"
                        class="h-8 w-full sm:w-96 pl-4 mt-1 mb-2 rounded-md border-2 border-slate-300"
                />
                <br />
                <button type="submit" class="mt-4">
                    <Button type="primary" text="Pridať"></Button>
                </button>
            </div>
        </form>
    </div>
</div>
