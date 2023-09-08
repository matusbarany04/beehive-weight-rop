import {writable} from "svelte/store";

const user = writable({});
export let message = writable("Loading...");

export default {
  async fetchUser() {
    try {
      let response = await fetch("/user/byToken");
      if (response.ok) {
        const userData = await response.json();
        loadData("user", userData.user);
        user.set(userData.user);
      } else {
        console.error('Failed to fetch user:', response.statusText);
      }
    } catch (error) {
      console.error('Error fetching user:', error);
    }
  },


  fetchBeehives: async () => {
    console.log("fetch")
    let promise = await fetch('/dashboardApi/getBeehives');
    let response = await promise.json();
    console.log(response)
    if (response.status === "ok") {
      loadData("beehives", response["beehives"]);
    }
  },

  fetchStatuses: () => {
    console.log("fetch")
    fetch('/dashboardApi/getData').then(r => r.json())
      .then(response => {
      //  beehive_data.set(response);
        loadData("statuses", response.data);
      });
  },

  getUser() {
    let userValue;
    user.subscribe(value => {
      userValue = value;
    })();
    return userValue;
  },

  getTemperatures: (beehive_id) => {
    return savedData["statuses"][beehive_id]["temps"];
  },
  getWeights: (beehive_id) => {
      return savedData["statuses"][beehive_id]["weights"];
  },
  getHumidities: (beehive_id) => {
    return savedData["statuses"][beehive_id]["humids"];
  },
  getBatteries: (beehive_id) => {
    return savedData["statuses"][beehive_id]["batteries"];
  },

  getWeight: (beehive_id) => {
    let weights = savedData["statuses"][beehive_id]["weights"];
    return weights[weights.length - 1];
  },
  getLastUpdateTime: (beehive_id) => {
    let timestamps = savedData["statuses"][beehive_id]["timestamps"];
    return timestamps[timestamps.length - 1];
  },

  getBattery: (beehive_id) => {
    let batteries = savedData["statuses"][beehive_id]["batteries"];
    return batteries[batteries.length - 1];
  },
}

let savedData = {};
let callbacks = [];

export function onLoad(dataTypes, callback) {
  if(!Array.isArray(dataTypes)) dataTypes = [dataTypes];
  
  let loadedData = getLoadedData(dataTypes);
  if (loadedData != null) callback(...loadedData);
  else {
    let newCallback = {dataTypes: dataTypes, func: callback};
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
  console.log(savedData);
  for (let callback of callbacks) {
    if (callback.dataTypes.includes(dataType)) {
      let loadedData = getLoadedData(callback.dataTypes);
      if (loadedData != null) callback.func(...loadedData);
    }
  }
}