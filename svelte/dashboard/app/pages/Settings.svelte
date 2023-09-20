<script>
    /**
     * @fileoverview This page handles general settings of the dashboard
     * @module Settings
     */
    import {onMount} from "svelte";
    import SettingsHeader from "../component/settings/SettingsHeader.svelte";
    import SettingsItem from "../component/settings/SettingsItem.svelte";
    import Toggle from "../component/settings/Toggle.svelte";
    import message from "../stores/message";
    import shared, {onLoad} from "../stores/shared";
    import Loading from "../../../components/pages/Loading.svelte";
    import {generateUUID} from "../../../components/lib/utils/static";
    import EditPanel from "../component/panel/EditPanel.svelte";
    import Button from "../../../components/Buttons/Button.svelte";
    import toast from "../../../components/Toast/toast";

    message.setMessage("Nastavenia");

    let settings = null;
    onLoad("settings", (settings_json) => {
        settings = settings_json;
    })


    function saveSettings() {
        fetch("/dashboardApi/settings/updateBatch", {
            method: "PUT", body:
                JSON.stringify(settings)
        }).then((response) => {
            toast.push("Settings saved!")
        });
    }
</script>

<svelte:head>
    <title>Analytika</title>
    <meta name="Analytika" content="Analytika"/>
</svelte:head>

<div class="absolute right-0 top-0 z-50 flex w-min justify-end gap-3 p-4">

    <div class="flex gap-4">

        <Button
                text={"Uložiť"}
                type={"confirm"}
                onClick={() => {
          saveSettings();
        }}
        />
    </div>

</div>

<form class="h-full w-full">
    {#if settings}

        <SettingsHeader title="Upozornenia"/>

        <SettingsItem
                title="Posielať upozornenia na mail"
                detail="Týmto nastavením zapnete/vypnete všetky nastavenia"
        >
            <Toggle bind:checked="{settings['send_notifications']}"/>
        </SettingsItem>


        <SettingsItem
                title="Posielať upozornenia iný mail"

        >
            <Toggle bind:checked="{settings['use_user_login_mail']}"/>
        </SettingsItem>
        
        {#if settings['use_user_login_mail'] === true}
            <SettingsItem
                    title="Email"
                    detail="Email na ktorý sa budú zasielať upozornenia"
                    inputPlaceholder="Váš email"
            >
                <input
                        type="text"
                        placeholder="Váš email"
                        bind:value="{settings['alt_mail']}"
                        class="h-8 w-96 rounded-md border-2 border-slate-300 px-4"
                />
            </SettingsItem>
        {/if}

        <SettingsItem
                title="Nerušiť medzi"
                detail="Čas medzi ktorým sa nebudú posielať upozornenia"
                inputPlaceholder="Váš email"
        >
            <div class="flex flex-col md:flex-row">
                <div class="flex flex-col">
                    <label for="time">čas od: </label>
                    <input
                            class="mr-8 h-8 w-44 rounded-md border-2 border-slate-300 px-4"
                            type="time"
                            id="time-input"
                            name="time"
                            step="3600"
                            min="00:00"
                            max="23:59"
                            bind:value="{settings['dont_disturb_from']}"
                            pattern="[0-2][0-9]:[0-5][0-9]"
                    />
                </div>
                <div class="flex flex-col">
                    <label for="time">čas do: </label>

                    <input
                            class="h-8 w-44 rounded-md border-2 border-slate-300 px-4"
                            type="time"
                            id="time-input"
                            name="time"
                            step="3600"
                            min="00:00"
                            max="23:59"
                            bind:value="{settings['dont_disturb_to']}"
                            pattern="[0-2][0-9]:[0-5][0-9]"
                    />
                </div>
            </div>
        </SettingsItem>


        <SettingsHeader title="Vlhkosť"></SettingsHeader>

        <SettingsItem
                title="Zvýšená vlhkosť vzduchu"
                detail="Pri zvýšenej vlhkosti Vám príde upozornenie"
        >
            <Toggle bind:checked="{settings['high_humidity']}"/>


        </SettingsItem>
        {#if settings['high_humidity'] === true}
            <SettingsItem detail="High humidity Threshold">
                <select
                        class="mr-4 h-8 w-72 rounded-md border-2 border-slate-300 bg-white px-4"
                        name="pets"
                        bind:value={settings['high_humidity_threshold']}
                        id="pet-select"
                >
                    <option value="">--Please choose an option--</option>
                    {#each Array(11) as _, index (index)}
                        <option value="{index * 10}"
                                selected={settings['high_humidity_threshold'] === (index * 10)}>
                            {index * 10}%
                        </option>
                    {/each}
                </select>
            </SettingsItem>
        {/if}


        <SettingsItem
                title="Znížená vlhkosť vzduchu"
                detail="Pri zníženej vlhkosti Vám príde upozornenie"
        >
            <Toggle bind:checked="{settings['low_humidity']}"/>
        </SettingsItem>
        {#if settings['low_humidity'] === true}
            <SettingsItem detail="Low humidity Threshold">
                <select
                        class="mr-4 h-8 w-72 rounded-md border-2 border-slate-300 bg-white px-4"
                        name="pets"
                        bind:value={settings['low_humidity_threshold']}
                        id="pet-select"
                >
                    <option value="">--Please choose an option--</option>
                    {#each Array(11) as _, index (index)}
                        <option value="{index * 10}"
                                selected={settings['low_humidity_threshold'] === (index * 10)}>
                            {index * 10}%
                        </option>
                    {/each}
                </select>
            </SettingsItem>
        {/if}
        
        <SettingsHeader title="Váha"></SettingsHeader>
        
        <!--weight -->
        <SettingsItem
                title="Zvýšená váha váhy"
                detail="Pri zvýšenej váhe Vám príde upozornenie"
        >
            <Toggle bind:checked="{settings['heavy_weight']}"/>


        </SettingsItem>
        {#if settings['heavy_weight'] === true}
            <SettingsItem detail="Heavy weight threshold">
                <select
                        class="mr-4 h-8 w-72 rounded-md border-2 border-slate-300 bg-white px-4"
                        name="pets"
                        bind:value={settings['heavy_weight_threshold']}
                        id="pet-select"
                >
                    <option value="">--Please choose an option--</option>
                    {#each Array(11) as _, index (index)}
                        <option value="{index * 10}"
                                selected={settings['heavy_weight_threshold'] === (index * 10)}>
                            {index * 10}kg
                        </option>
                    {/each}
                </select>
            </SettingsItem>
        {/if}


        <SettingsItem
                title="Znížená vlhkosť vzduchu"
                detail="Pri zníženej váhe Vám príde upozornenie"
        >
            <Toggle bind:checked="{settings['light_weight']}"/>
        </SettingsItem>
        {#if settings['light_weight'] === true}
            <SettingsItem detail="Low weight threshold">
                <select
                        class="mr-4 h-8 w-72 rounded-md border-2 border-slate-300 bg-white px-4"
                        name="pets"
                        bind:value={settings['light_weight_threshold']}
                        id="pet-select"
                >
                    <option value="">--Please choose an option--</option>
                    {#each Array(11) as _, index (index)}
                        <option value="{index * 10}"
                                selected={settings['light_weight_threshold'] === (index * 10)}>
                            {index * 10}kg
                        </option>
                    {/each}
                </select>
            </SettingsItem>
        {/if}
        
        
        <SettingsItem
                title="Batéria úľov"
                detail="Pri nízkej báterií Vám príde upozornenie"
        >
            <select
                    class="mr-4 h-8 w-72 rounded-md border-2 border-slate-300 bg-white px-4"
                    name="pets"
                    id="pet-select"
            >
                <option value="">--Please choose an option--</option>
                <option value="50" selected={settings['battery_low_threshold'] === 50}>50%</option>
                <option value="20" selected={settings['battery_low_threshold'] === 20}>20%</option>
                <option value="10" selected={settings['battery_low_threshold'] === 10}>10%</option>
                <option value="0" selected={settings['battery_low_threshold'] === 0}>0%</option>
            </select>
            <Toggle/>
        </SettingsItem>
    {:else }
        <Loading/>
    {/if}
</form>
