const errorTypes = [
  { type: "NoDataError", message: "Chyba pri načítaní dát to karty" },
];

/**
 * Returns an array of error types with corresponding messages
 * @function
 */
export function getErrorTypes() {
  return errorTypes;
}

/**
 * Returns message if error type exists
 * @function
 * @param {string} type type of error message
 */
export function getErrorMessageByType(type) {
  let output = errorTypes.find((error) => error.type == type);
  if (!output) {
    return "Chyba pri načítaní chyby!";
  }
  return output.message;
}
