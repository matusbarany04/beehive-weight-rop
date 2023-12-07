export const TW_BREAKPOINTS = {
  sm: 640,
  md: 768,
  lg: 1024,
  xl: 1280,
  "2xl": 1536,
};

export default {
  /**
   * Function checks equality be casting both to a string and returns boolean if they match
   * Limitations:
   *  Order has to be same for both jsons otherwise they will be not equal
   * @param a {json}
   * @param b {json}
   * @return {boolean}
   */
  jsonFlatEqual: function (a, b) {
    return JSON.stringify(a) === JSON.stringify(b);
  },

  /**
   * Function copies values from the first json object to the second one
   * only if the second one has the properties (excluding specified properties).
   *
   * @param source {Object} - The source JSON object
   * @param target {Object} - The target JSON object
   * @param excludeProperties {Array} - The list of properties to exclude from copying
   * @return {Object} - The updated target JSON object
   */
  copyKeepReference(source, target, excludeProperties = []) {
    for (const key in source) {
      // Check if the property exists in the target object and is not in the exclusion list
      if (target.hasOwnProperty(key) && !excludeProperties.includes(key)) {
        // Copy the value from the source to the target
        target[key] = source[key];
      }
    }
    return target;
  },

  /**
   * Replaces placeholders in a template string with provided values.
   *
   * @param {string} template - The template string with placeholders.
   * @param {...string} args - The values to replace the placeholders in the template.
   * @returns {string} The formatted string.
   */
  formatString(template, ...args) {
    return template.replace(/%s/g, () => args.shift());
  },

  /**
   * Converts an array of keys into an array of key-value pairs.
   * @param {Array<string>} keys - An array of keys.
   * @param {any} value - The common value to be associated with each key.
   * @returns {Array<Array<string, any>>} An array of key-value pairs.
   */
  arrayToKeyValuePairs(keys) {
    return keys.map((key) => [key, key]);
  },

  /**
   * Function checks equality be looping over their values and returns boolean if they match
   * @param json1 {json}
   * @param json2 {json}
   * @return {boolean}
   */
  deepCompareJson: function (json1, json2) {
    if (
      Object.prototype.toString.call(json1) ===
      Object.prototype.toString.call(json2)
    ) {
      if (
        Object.prototype.toString.call(json1) === "[object Object]" ||
        Object.prototype.toString.call(json1) === "[object Array]"
      ) {
        if (Object.keys(json1).length !== Object.keys(json2).length) {
          return false;
        }
        return Object.keys(json1).every(function (key) {
          return this.deepCompareJson(json1[key], json2[key]);
        });
      }
      return json1 === json2;
    }
    return false;
  },
};

export const generateUUID = () => {
  let d = new Date().getTime(),
    d2 =
      (typeof performance !== "undefined" &&
        performance.now &&
        performance.now() * 1000) ||
      0;
  return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, (c) => {
    let r = Math.random() * 16;
    if (d > 0) {
      r = (d + r) % 16 | 0;
      d = Math.floor(d / 16);
    } else {
      r = (d2 + r) % 16 | 0;
      d2 = Math.floor(d2 / 16);
    }
    return (c === "x" ? r : (r & 0x7) | 0x8).toString(16);
  });
};

/** nepouzivat priamo toto je len fallback, volana iba z copyTextToClipboard*/
function fallbackCopyTextToClipboard(text) {
  var textArea = document.createElement("textarea");
  textArea.value = text;

  // Avoid scrolling to bottom
  textArea.style.top = "0";
  textArea.style.left = "0";
  textArea.style.position = "fixed";

  document.body.appendChild(textArea);
  textArea.focus();
  textArea.select();

  try {
    var successful = document.execCommand("copy");
    var msg = successful ? "successful" : "unsuccessful";
    console.log("Fallback: Copying text command was " + msg);
  } catch (err) {
    console.error("Fallback: Oops, unable to copy", err);
  }

  document.body.removeChild(textArea);
}

/** only after component mount */
export function copyTextToClipboard(text) {
  if (!navigator.clipboard) {
    fallbackCopyTextToClipboard(text);
    return;
  }
  navigator.clipboard.writeText(text).then(
    function () {
      console.log("Async: Copying to clipboard was successful!");
    },
    function (err) {
      console.error("Async: Could not copy text: ", err);
    },
  );
}

/**
 * Validates an email field
 * @file lib/utils/helpers/input.validation.ts
 * @param {string} email - The email to validate
 */
export const isValidEmail = (email) => {
  const EMAIL_REGEX =
    /[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?/;
  return EMAIL_REGEX.test(email.trim());
};

/**
 * Validates a strong password field
 * @file lib/utils/helpers/input.validation.ts
 * @param {string} password - The password to validate
 */

export const isValidPasswordStrong = (password) => {
  const strongRegex = new RegExp(
    "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])(?=.{8,})",
  );

  return strongRegex.test(password.trim());
};

/**
 * Validates a medium password field
 * @file lib/utils/helpers/input.validation.ts
 * @param {string} password - The password to validate
 */
export const isValidPasswordMedium = (password) => {
  const mediumRegex = new RegExp(
    "^(((?=.*[a-z])(?=.*[A-Z]))|((?=.*[a-z])(?=.*[0-9]))|((?=.*[A-Z])(?=.*[0-9])))(?=.{6,})",
  );

  return mediumRegex.test(password.trim());
};

/**
 * Determines if an object (often a form object) is empty.
 *
 * @param {Object} obj - Object to check.
 * @returns {boolean} - True if the object has no properties, false otherwise.
 *
 * @example
 * const formData = {
 *   name: 'John',
 *   age: 25
 * };
 * console.log(isEmpty(formData));  // Outputs: false
 */
export function isEmpty(obj) {
  for (const _i in obj) {
    return false;
  }
  return true;
}

import pkg from "file-saver";

const { saveAs } = pkg;
export const triggerDownloadCsv = function (data, filename) {
  // // Creating a Blob for having a csv file format
  // // and passing the data with type
  const blob = new Blob([data], { type: "text/csv" });

  // // Creating an object for downloading url
  const url = window.URL.createObjectURL(blob);

  // // Creating an anchor(a) tag of HTML
  // const a = document.createElement("a");

  // // Passing the blob downloading url
  // a.setAttribute("href", url);

  // // Setting the anchor tag attribute for downloading
  // // and passing the download file name
  // a.setAttribute("download", filename + ".csv");

  // // Performing a download with click
  // a.click();
  console.log("Downloading pending.. ");
  // var file = new File(["Hello, world!"], "hello world.txt", {type: "text/plain;charset=utf-8"});
  saveAs(blob, filename + ".csv");
};

export const jsonToCsv = function (data) {
  const items = data;
  const replacer = (key, value) => (value === null ? "" : value); // specify how you want to handle null values here
  const header = Object.keys(items[0]);
  return [
    header.join(","), // header row first
    ...items.map((row) =>
      header
        .map((fieldName) => JSON.stringify(row[fieldName], replacer))
        .join(","),
    ),
  ].join("\r\n");
};

export function getUnitByType(type) {
  const typeToUnitMap = {
    battery: "%",
    humidity: "%",
    weight: "kg",
    temperature: "°C",
  };

  return typeToUnitMap[type] || "";
}

const MILLISECONDS_IN_HOUR = 1000 * 60 * 60;
const MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;
const MILLISECONDS_IN_WEEK = 1000 * 60 * 60 * 24 * 7;
const MILLISECONDS_IN_MONTH = 1000 * 60 * 60 * 24 * 30;
const MILLISECONDS_IN_YEAR = 1000 * 60 * 60 * 24 * 365;
const MILLISECONDS_IN_ETERNITY = 6.627096e13; //2100 years

const TIME_UNITS = {
  hour: MILLISECONDS_IN_HOUR,
  day: MILLISECONDS_IN_DAY,
  week: MILLISECONDS_IN_WEEK,
  month: MILLISECONDS_IN_MONTH,
  year: MILLISECONDS_IN_YEAR,
  eternity: MILLISECONDS_IN_ETERNITY,
};

export function getDefaultTimeUnit() {
  return TIME_UNITS.eternity;
}

/**
 * Converts a time string to milliseconds.
 * @param {string} timeString - The time unit string ("hour", "day", "week", "month", "year").
 * @returns {number} - The corresponding time in milliseconds.
 * @throws {Error} - If the timeString is not a valid option.
 */
export function stringToTime(timeString) {
  if (TIME_UNITS[timeString] != null) {
    return TIME_UNITS[timeString];
  } else {
    throw new Error("timeString is not a valid option!");
  }
}

/**
 * Gets the current timestamp in milliseconds.
 * @returns {number} - The current timestamp in milliseconds.
 */
export function getMillisNow() {
  return Date.now();
}

/**
 * Calculates the timestamp in the past based on a time string.
 * @param {string} timeString - The time unit string ("hour", "day", "week", "month", "year").
 * @returns {number} - The timestamp in the past in milliseconds.
 */
export function nowMinusTimeString(timeString) {
  return getMillisNow() - stringToTime(timeString);
}
