<script>
  import Button from "./Buttons/Button.svelte";

  export let showModal; // boolean

  let dialog; // HTMLDialogElement

  $: if (dialog) showModal ? dialog.showModal() : dialog.close();
</script>

<!-- svelte-ignore a11y-click-events-have-key-events a11y-no-noninteractive-element-interactions -->
<dialog
  bind:this={dialog}
  class="rounded-md max-w-lg bg-[white] overflow-visible"
  on:close={() => (showModal = false)}
  on:click|self={() => dialog.close()}
>
  <!-- svelte-ignore a11y-no-static-element-interactions -->
  <div class="p-8" on:click|stopPropagation>
    <slot name="header" />
    <hr />

    <slot />

    <hr />

    <div class="flex gap-4 mt-4 justify-start">
      <Button
        type="secondary"
        autofocus
        onClick={() => dialog.close()}
        text="Zatvoriť okno"
      />
      <slot name="footer" />
    </div>
  </div>
</dialog>

<style>
  dialog::backdrop {
    background: rgba(0, 0, 0, 0.3);
  }

  dialog[open] {
    animation: zoom 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
  }
  @keyframes zoom {
    from {
      transform: scale(0.95);
    }
    to {
      transform: scale(1);
    }
  }
  dialog[open]::backdrop {
    animation: fade 0.2s ease-out;
  }
  @keyframes fade {
    from {
      opacity: 0;
    }
    to {
      opacity: 1;
    }
  }
</style>
