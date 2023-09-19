import { writable, get } from "svelte/store";

export let messageWritable = writable("Message wasn't set");

/**
 * Set of message utility functions
 */
export default {
  /**
   * Return a text of a message that was last set
   * @returns {string}
   */
  getMessage() {
    return get(messageWritable);
  },
  /**
   * sets a new message to be displayed
   * @param value {string}
   */
  setMessage(value) {
    messageWritable.set(value);
  },
  /**
   * Checks if any message is present
   * @returns {boolean}
   */
  isMessageSet() {
    return get(messageWritable).length > 0;
  },
  /**
   * Returns a reference to writable object
   * @returns {writable}
   */
  getMessageRef() {
    return messageWritable;
  },
  /**
   * Resets message to empty string
   */
  resetMessage() {
    messageWritable.set("");
  },
};
