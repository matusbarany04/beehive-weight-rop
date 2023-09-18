import { toast } from "@zerodevx/svelte-toast";

export default {
  push: function (type = "default") {
    toast.push("Styled with custom class", { classes: ["toast-" + type] });
  },
};
