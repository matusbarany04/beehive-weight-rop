import { writable, get } from "svelte/store";

export let messageWritable = writable("Message wasn't set");
export default {
  getMessage() {
    return get(messageWritable);
  },
  setMessage(value) {
    messageWritable.set(value);
  },
  isMessageSet() {
    return get(messageWritable).length > 0;
  },
  getMessageRef() {
    return messageWritable;
  },
  resetMessage() {
    messageWritable.set("");
  },
};
