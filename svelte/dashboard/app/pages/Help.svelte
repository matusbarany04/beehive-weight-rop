<script>
    import {dataHandler} from "../../../components/cards/dataHandler";
    import shared from "../stores/shared";


</script>

<h1 class="text-center text-5xl">pomoc !!!</h1>
<p>{JSON.stringify(shared.getUser())}</p>

<p>{dataHandler.getAllBeehiveData()}</p>
