<script>
  import { generateUUID } from "../../../../components/lib/utils/staticFuncs";

  export let checked = true;

  export let action = function () {};

  const id = generateUUID();
  const handleCheckboxChange = () => {
    checked = !checked;
    action(checked);
  };
</script>

<div class="inline-flex items-center">
  <div class="relative inline-block h-4 w-8 cursor-pointer rounded-full">
    <input
      {id}
      type="checkbox"
      {checked}
      on:change={handleCheckboxChange}
      class="peer absolute h-4 w-8 cursor-pointer appearance-none rounded-full bg-tertiary-500 transition-colors duration-300 checked:bg-secondary-500 peer-checked:bg-tertiary-500 peer-checked:before:bg-tertiary-500"
    />
    <label
      for={id}
      class="before:content[''] absolute -left-1 top-2/4 h-5 w-5 -translate-y-2/4 cursor-pointer rounded-full border border-tertiary-100 bg-white shadow-md transition-all duration-300 before:absolute before:left-2/4 before:top-2/4 before:block before:h-10 before:w-10 before:-translate-x-2/4 before:-translate-y-2/4 before:rounded-full before:bg-tertiary-500 before:opacity-0 before:transition-opacity hover:before:opacity-10 peer-checked:translate-x-full peer-checked:border-tertiary-200 peer-checked:before:bg-secondary-500"
    >
      <div
        class="left-2/4 top-2/4 inline-block -translate-x-2/4 -translate-y-2/4 rounded-full p-5"
        data-ripple-dark="true"
      />
    </label>
  </div>
</div>
