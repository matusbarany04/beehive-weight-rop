<script>
  import CardRoot from "./components/CardRoot.svelte";
  export let cardStates;
  export let onDragEnd; // function
  export let onDragStart; // function
  let component = "DoubleGraph";
  let error = null;
</script>

<CardRoot {onDragStart} {onDragEnd} {cardStates} {component} {error}>
  <div class="relative h-full w-full">
    <div class="dividerH" />
    <div class="grapCont">
      <div class="graph">
        <h1 class="value">80%</h1>
        <div class="barCont">
          <div class="bar bg-primary-100" style:--height="80%" />
          <!-- style:--base-color="#316AFF" -->
        </div>
        <p class="date">dnes</p>
      </div>
      <div class="dividerV" />
      <div class="graph">
        <h1 class="value">40%</h1>
        <div class="barCont">
          <div class="bar bg-secondary-500" style:--height="40%" />
          <!-- style:--base-color="#EECA6F" -->
        </div>
        <p class="date">dnes</p>
      </div>
    </div>
  </div>
  <!-- <div class="versus">vs</div> -->
</CardRoot>

<style lang="scss">
  .grapCont {
    display: flex;
    height: 100%;
  }

  .barCont {
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: end;
    flex: 1;
  }
  .graph {
    height: 100%;
    flex: 1;
    flex-direction: column;
    display: flex;
    align-items: end;
    justify-content: center;
  }
  .bar {
    box-sizing: border-box;
    width: 60%;
    border-radius: 8px;
    height: var(--height);
  }
  .dividerH {
    margin: 0 auto;
    width: 80%;
    height: 1px;
    background-color: var(--color-detail-gray);
  }
  .value {
    margin: 0 auto;
    margin-top: 10px;
  }

  .date {
    margin: 5px auto;
  }
  .dividerV {
    height: 90%;
    width: 1px;
    background-color: var(--color-detail-gray);
  }
</style>
