import { toast } from "@zerodevx/svelte-toast";

export default {
  push: function (message, type = "default") {
    toast.push(message, { duration: 4000, classes: ["toast-" + type] });
  },
};
