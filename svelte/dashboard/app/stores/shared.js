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
        beehive_data.set(response);
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
}

let savedData = {};
let callbacks = [];

export function onLoad(dataTypes, callback) {
  let loadedData = getLoadedData(dataTypes);
  if (loadedData != null) callback(...loadedData);
  else {
    let newCallback = {dataTypes: dataTypes, func: callback};
    callbacks.push(newCallback);
  }

  console.log(callbacks);
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