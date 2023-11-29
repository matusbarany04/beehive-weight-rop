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
      console.log("changed ", beehive_id_array);
      if (beehive_id_array == ["all"] || beehive_id_array === null) {
        // TODO this should be union of all types, for now this is okay
        typeChoiceList = BeehiveObj.getNonDetachableTypesAsKeyValuePairs(true);
      } else {
        console.log("beehive_id_array", beehive_id_array);
        typeChoiceList = BeehiveObj.getUnionOfCurrentDataTypesAsKeyValuePairs(
          beehive_id_array,
          false,
        );

        // shared
        //   .getBeehivesByIds(beehive_id_array)
        //   .map(beehive => beehive?.getCurrentDataTypesAsKeyValuePairs(true));

        console.log("typeChoiceList", typeChoiceList);
      }

      // if choice is not in new typeChoiceList reset back to primary
      if (!typeChoiceList.some(([key]) => key === typeChoice)) {
        console.log("Not inside ", typeChoice, typeChoiceList);
        typeChoice = BeehiveObj.getPrimaryDataType();
      }

      console.log("checking all!", beehive_id_array);
      if (beehive_id_array.includes("all")) {
        console.log("heyyyy");
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
