<script>
  import MultiSelectDropdownInput from "../../../../../components/Inputs/MultiSelectDropdownInput.svelte";

  export let typeChoice = "weight";

  export let beehiveId = "all";

  import { BeehiveObj } from "../../../stores/Beehive";
  import shared from "../../../stores/shared";
  import DropdownInput from "../../../../../components/Inputs/DropdownInput.svelte";

  let typeChoiceList = [];

  let updateDataTypeList = (beehive_id_array) => {
    try {
      if (beehive_id_array == ["all"] || beehive_id_array === null) {
        // TODO this should be union of all types, for now this is okay
        typeChoiceList = BeehiveObj.getNonDetachableTypesAsKeyValuePairs(true);
      } else {
        typeChoiceList = BeehiveObj.getUnionOfCurrentDataTypesAsKeyValuePairs(
          beehive_id_array,
          true,
        );

        // shared
        //   .getBeehivesByIds(beehive_id_array)
        //   .map(beehive => beehive?.getCurrentDataTypesAsKeyValuePairs(true));

      }

      // if choice is not in new typeChoiceList reset back to primary
      if (!typeChoiceList.some(([key]) => key === typeChoice)) {
        typeChoice = BeehiveObj.getPrimaryDataType();
      }

      if (beehive_id_array.includes("all")) {
        
        typeChoiceList = BeehiveObj.getNonDetachableTypesAsKeyValuePairs(true);
      }
    } catch (e) {
      console.error(e);
    }
  };

  let typeChanged = (type) => {
    typeChoice = type;
  };

  updateDataTypeList(beehiveId);
</script>

<MultiSelectDropdownInput
  label="Váha"
  className=""
  name="beehive_id"
  onChange={updateDataTypeList}
  value={beehiveId}
  small={"Váha pre ktorú sa budú zobrazovať dáta"}
  options={[...shared.getBeehiveIdsWithNames()]}
  default_option={["all", "all"]}
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
