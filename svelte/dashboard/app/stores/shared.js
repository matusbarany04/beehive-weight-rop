import { writable } from "svelte/store";
import { dataHandler } from "../../../components/dashboard/cards/dataHandler";

const user = writable({});
export let message = writable("Loading...");

export let dataTypes = ["temperature", "humidity", "weight"];
export let fromValues = ["all", "hour", "day", "week", "month", "year"];
export let topValues = ["now"];

export default {
  fetchUser: async function () {
    try {
      let response = await fetch("/user/byToken");
      if (response.ok) {
        const userData = await response.json();
        loadData("user", userData.user);
        user.set(userData.user);
      } else {
        console.error("Failed to fetch user:", response.statusText);
      }
    } catch (error) {
      console.error("Error fetching user:", error);
    }
  },

  fetchBeehives: async function () {
    let promise = await fetch("/dashboardApi/getBeehives");
    let response = await promise.json();

    if (response.status === "ok") {
      loadData("beehives", response["beehives"]);
    }
  },

  fetchStatuses: function () {
    fetch("/dashboardApi/getData")
      .then((r) => r.json())
      .then((response) => {
        //  beehive_data.set(response);
        loadData("statuses", response.data);
      });
  },

  getUser: function () {
    let userValue;
    user.subscribe((value) => {
      userValue = value;
    })();
    return userValue;
  },
  getStatuses: function () {
    return savedData["statuses"];
  },
  getBeehives: function () {
    return savedData["beehives"];
  },
  getBeehiveById: function (beehive_id) {
    let beehives = this.getBeehives();

    for (const beehive of beehives) {
      if (beehive.token === beehive_id) return beehive;
    }
    return null;
  },
  getStatusesById: function (beehive_id) {
    return this.getStatuses()[beehive_id];
  },
  getStatusesByType: function (type, beehive_id) {
    return this.getStatusesById(beehive_id)[type];
  },

  getBeehiveIdsWithNames: function () {
    return savedData["beehives"];
  },

  getDataByType: function (type, beehive_id, with_timestamp, from) {
    let statuses = this.getStatusesByType(type, beehive_id);
    let timestamps = this.getTimestamps(beehive_id);
    let out = timestamps.map((e, i) => [e, statuses[i]]);
    if (from != null) {
      out = [];
      for (let i = 0; i < statuses.length; i++) {
        if (timestamps[i] > this.nowMinusFrom(from)) {
          if (with_timestamp) {
            out.push([timestamps[i], statuses[i]]);
          } else {
            out.push(statuses[i]);
          }
        }
      }
    }

    return out;
  },

  getTemperatures: function (beehive_id) {
    return savedData["statuses"][beehive_id]["temperature"];
  },
  getWeights: function (beehive_id) {
    return savedData["statuses"][beehive_id]["weight"];
  },
  getHumidities: function (beehive_id) {
    return savedData["statuses"][beehive_id]["humidity"];
  },
  getBatteries: function (beehive_id) {
    return savedData["statuses"][beehive_id]["battery"];
  },
  getWeight: function (beehive_id) {
    let weights = savedData["statuses"][beehive_id]["weight"];
    return weights[weights.length - 1];
  },
  getLastUpdateTime: function (beehive_id) {
    let timestamps = savedData["statuses"][beehive_id]["timestamp"];
    return timestamps[timestamps.length - 1];
  },
  getTimestamps: function (beehive_id) {
    return savedData["statuses"][beehive_id]["timestamp"];
  },
  getBattery: function (beehive_id) {
    return savedData["statuses"][beehive_id]["battery"][
      savedData["statuses"][beehive_id]["battery"].length - 1
    ];
  },

  nowMinusFrom: function (from) {
    return new Date(new Date().getTime() - fromValueToTimestamp(from));
  },
};

let savedData = {};
let callbacks = [];

export function onLoad(dataTypes, callback) {
  if (!Array.isArray(dataTypes)) dataTypes = [dataTypes];

  let loadedData = getLoadedData(dataTypes);
  if (loadedData != null) callback(...loadedData);
  else {
    let newCallback = { dataTypes: dataTypes, func: callback };
    callbacks.push(newCallback);
  }
}

function getLoadedData(dataTypes) {
  let results = [];
  for (let dataType of dataTypes) {
    if (savedData[dataType] === undefined) return null;
    else results.push(savedData[dataType]);
  }

  return results;
}

function loadData(dataType, data) {
  savedData[dataType] = data;
  for (let callback of callbacks) {
    if (callback.dataTypes.includes(dataType)) {
      let loadedData = getLoadedData(callback.dataTypes);
      if (loadedData != null) callback.func(...loadedData);
    }
  }
}

function fromValueToTimestamp(from) {
  switch (from) {
    case "all":
      return 0;
    case "hour":
      return 1000 * 60 * 60;
    case "day":
      return 1000 * 60 * 60 * 24;
    case "week":
      return 1000 * 60 * 60 * 24 * 7;
    case "month":
      return 1000 * 60 * 60 * 24 * 30;
    case "year":
      return 1000 * 60 * 60 * 24 * 365;
    default:
      console.error("Unknown from value: " + from);
      break;
  }
}
