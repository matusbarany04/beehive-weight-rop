<script>
  export let beehive_value = false;
  export let typeChoice = "weight";

  export let beehiveId = "all";

  import { BeehiveObj } from "../../../stores/Beehive";
  import shared from "../../../stores/shared";
  import DropdownInput from "../../../../../components/Inputs/DropdownInput.svelte";

  let typeChoiceList = [];

  let updateDataTypeList = (beehive_id) => {
    if (beehive_id === "all") {
      // TODO this should be union of all types, for now this is okay
      typeChoiceList = BeehiveObj.getNonDetachableTypesAsKeyValuePairs(true);
    } else {
      typeChoiceList = shared
        .getBeehiveById(beehive_id)
        .getCurrentDataTypesAsKeyValuePairs(true);
      
    }

    // if choice is not in new typeChoiceList reset back to primary
    if (!typeChoiceList.some(([key]) => key === typeChoice)) {
      typeChoice = BeehiveObj.getPrimaryDataType();
    }
  };

  let typeChanged = (type) => {
    typeChoice = type;
  };

  updateDataTypeList(beehiveId);
</script>

<DropdownInput
  label="Váha"
  className=""
  name="beehive_id"
  onChange={updateDataTypeList}
  value={beehiveId}
  small={"Váha pre ktorú sa budú zobrazovať dáta"}
  options={[["all", "all"], ...shared.getBeehiveIdsWithNames()]}
/>
<!--  TODO ERROR add back options without crashing the whole thing -->
<!--  -->

<DropdownInput
  label="Typ dát"
  name="data_type"
  value={typeChoice}
  onChange={typeChanged}
  options={typeChoiceList}
/>
