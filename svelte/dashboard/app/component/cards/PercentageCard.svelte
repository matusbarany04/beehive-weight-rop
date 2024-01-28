<script>
  import CardRoot from "./components/CardRoot.svelte";
  import shared from "../../stores/shared";
  import DropdownInput from "../../../../components/Inputs/DropdownInput.svelte";
  import {
    generateRandomText,
    generateUUID,
    getUnitByType,
  } from "../../../../components/lib/utils/staticFuncs";
  import BeehiveTypeForm from "./forms/BeehiveTypeForm.svelte";
  import fitty from "fitty";

  import { onMount } from "svelte";

  export let cardStates;

  export let contentEditable = true;

  let component = "PercentageCard";
  let error = null;
  let innerError = null;
  let w = 10;
  let h;
  $: size = Math.min(h, w) / 2 / (innerError == null ? 1 : 1.8);
  let value = 0;

  function loadAllBeehives(type) {
    let beehiveKeys = Object.keys(shared.getBeehives());
    let sum = 0;
    let count = 0;

    for (const beehiveKey of beehiveKeys) {
      let beehive = shared.getBeehives()[beehiveKey];
      let lastData = beehive.getLastDataByType(type);

      if (!isNaN(lastData)) {
        sum += lastData;
        count++;
      }
    }
    let average = count > 0 ? sum / count : 0;

    average =
      Number(average) === parseInt(average)
        ? Number(average)
        : Number(average).toFixed(1);

    value = average + getUnitByType(type);
  }

  try {
    if (cardStates.data === "dummy" || cardStates.data == []) {
      cardStates.title = "Priemer všetkých váh";
      cardStates.data = [
        {
          type: "weight",
          beehive_id: "all",
          from: "week",
          till: "now",
        },
      ];

      loadAllBeehives("weight");
    }

    if (!cardStates?.data) {
      error = "NoDataError";
    } else {
      cardStates.data.forEach((element) => {
        if (element.beehive_id === "all") {
          loadAllBeehives(element.type);
        } else {
          if (element.type === "dummy") {
            value = ":/";
            return; // continue to the next iteration
          }

          let beeData;
          if (element.data === undefined) {
            beeData = shared
              .getBeehiveById(element.beehive_id)
              .getLastDataByType(element.type);
          } else {
            beeData = element.data;
          }

          if (beeData != null) {
            if (!isNaN(beeData)) {
              value =
                Number(beeData) === parseInt(beeData)
                  ? Number(beeData)
                  : Number(beeData).toFixed(1);

              value = value || "NoData";

              if (value !== "error" && value !== "NoData") {
                if (element.unit === undefined) {
                  value += getUnitByType(element.type);
                } else {
                  value += element.unit;
                }
              } else {
                innerError = "NoData";
              }
            } else {
              value = beeData;
            }
          } else {
            value = "NoData";
            innerError = "NoData";
          }
        }
      });
    }
  } catch (e) {
    console.error("Percentage card error ", e, cardStates);
    error = "CardStateProcessError";
  }
  // key value pairs of data types of selected beehive
  // default with be non-detachable types
  let data_types = [
    ["weight", "Váha"],
    ["temperature", "Teplota"],
    ["humidity", "Vlhkosť"],
  ];

  function onBeehiveInputChange() {
    // get beehive id
    // fetch all non-detachable and detachable data into key - value pairs
    // update data_types
  }

  var fitties = null;
  let textid = generateRandomText();
  onMount(() => {
    fitties = fitty(`#${textid}`);

    // get element reference of first fitty
    var myFittyElement = fitties[0].element;

    myFittyElement.addEventListener("fit", function (e) {
      // log the detail property to the console
      console.log(e.detail);
    });

    setTimeout(() => {
      fitties[0].fit();
    }, 500);
    // // force refit
    //
    // // force synchronous refit
    // fitties[0].fit({ sync: true });
  });


  function padStringToLength(inputString, minLength) {
    if (inputString.length >= minLength) {
      return inputString;
    }

    const paddingLength = minLength - inputString.length;
    const leftPadding = Math.floor(paddingLength / 2);
    const rightPadding = Math.ceil(paddingLength / 2);

    const paddedString = ' '.repeat(leftPadding) + inputString + ' '.repeat(rightPadding);
    return paddedString;
  }

  // Example usage:
  const originalString = "ok";
  const minLength = 4;
  const paddedResult = padStringToLength(originalString, minLength);

  console.log(paddedResult);

  
</script>

<!-- theme="dashed" -->
<CardRoot
  updateSettings={(formData) => {
    if (formData.title === cardStates.title) {
      formData.title = "a";
    }
    return {
      status: "success",

      data: [
        {
          name: formData.get("data_type"), // TODO make translatable
          type: formData.get("data_type"),
          beehive_id: formData.get("beehive_id"),
        },
      ],
    };
  }}
  {contentEditable}
  {error}
  {component}
  {cardStates}
>
  <div
    class="flex h-full w-full flex-1 items-center justify-center"
    bind:clientWidth={w}
    bind:clientHeight={h}
  >
    <div class="mb-4 py-32 flex w-full items-center justify-center font-bold">
      <div class="h-full w-full">
        <h1 id={textid} class="fit overflow-hidden whitespace-nowrap text-center">
          {padStringToLength(value,6).replace(/ /g, '\u00a0')}
        </h1>
      </div>
    </div>
  </div>
  
  <div class="" slot="customSettings">
    {#if true}
      <!--error == null && value !== "error" && value !== "NoData"-->
      <BeehiveTypeForm
        typeChoice={cardStates.data[0].type || "weight"}
        beehive_value={cardStates.data[0].beehive_id || "all"}
        beehiveId={cardStates.data[0].beehive_id || "all"}
      ></BeehiveTypeForm>
    {/if}
  </div>
</CardRoot>
