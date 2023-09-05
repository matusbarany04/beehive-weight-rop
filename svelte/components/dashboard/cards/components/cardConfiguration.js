/**
 * @param {array} modes Modes that card can be in 
 */
export const modes = ["default", "add", "edit", "static"];

/**
 * Returns the default mode called 'default' 
 * @function 
 */
export function getDefaultMode() {
  return modes[0];
}

