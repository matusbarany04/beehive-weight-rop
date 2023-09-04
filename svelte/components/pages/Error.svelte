<script>
    import { page } from "$app/stores";
</script>

<div class="w-screen h-screen grid place-items-center bg-tertiary-100">
    <h1>{$page.status} {$page.error.message}</h1>
</div>