import {get, writable} from "svelte/store";
import {getContext} from "svelte";
import {isEmpty} from "../../lib/utils/static";

let beehive_data = writable([]);

export let dataTypes = ["temperature", "humidity", "weight"];
export let fromValues = ["all", "hour", "day", "week", "month", "year"];
export let topValues = ["now"];
/**
 * load data
 * Only usable on server side
 * @param {*} token
 */
export let timeframeFrom = [];

function fromValueToTimestamp(from) {
    switch (from) {
        case "all":
            return 0;
        case "hour":
            return 1000 * 1 * 60 * 60;
        case "day":
            return 1000 * 1 * 60 * 60 * 24;
        case "week":
            return 1000 * 1 * 60 * 60 * 24 * 7;
        case "month":
            return 1000 * 1 * 60 * 60 * 24 * 30;
        case "year":
            return 1000 * 1 * 60 * 60 * 24 * 365;
        default:
            console.error("Unknown from value: " + from);
            break;
    }
}

let callbacks = [];

export function onDataLoaded(callback) {
  callbacks.push(callback);
  let bee_data  = dataHandler.getRawBeehiveData();
  
  if (!isEmpty(bee_data)) {
    callback(bee_data);
  } else {
    callbacks.push(callback);
  }
}

export const dataHandler = {
    fetchData: () => {
      console.log("fetch")
        fetch('/dashboardApi/getData').then(r => r.json())
          .then(response => {
            beehive_data.set(response);
            callbacks.forEach(callback => callback(response));
          });
    },
  
    nowMinusFrom: (from) => {
        return new Date(new Date().getTime() - fromValueToTimestamp(from));
    },
    dataToTableFormat: () => {
        let bee_data = dataHandler.getAllBeehiveData();
        let output = [];
        bee_data.forEach((element) => {
            output.push({
                name: element.name,
                token: element.token,
                timestamp: element?.statuses[0]?.timestamp,
                weight: element?.statuses[0]?.weight,
                battery: element?.statuses[0]?.battery,
                status: element?.statuses[0]?.status,
            });
        });
        return output;
    },
    loadData: async () => {
        beehive_data.set(getContext("beehive_data") ?? []);
        // console.log("temperature", dataHandler.getTemperatures("NY17IS0J9RKMRFP3"));
    },
    getAllBeehiveData: () => {
      return dataHandler.getRawBeehiveData().data;
    },
    getRawBeehiveData: () => {
      return get(beehive_data);
    },
    getBeehiveData: (beehive_id) => {
        let bee_data = dataHandler.getAllBeehiveData();

        const index = bee_data
            .map(function (e) {
                return e.token;
            })
            .indexOf(beehive_id);

        if (index < 0) {
            console.error("beehive not found for id", beehive_id);
        }

        return bee_data[index];
    },
    getBeehiveIdsWithNames: function () {
        let bee_data = dataHandler.getAllBeehiveData();

        return bee_data.map(function (e) {
            return [e.token, e.name];
        });
    },
    getDataByType: (type, beehive_id, with_timestamp, from, to) => {
        let beehive = dataHandler.getBeehiveData(beehive_id);
        console.log(beehive);
        let statuses = beehive?.statuses;
        if (from != null) {
            statuses = [];
            for (let i = 0; i < beehive?.statuses.length; i++) {
                const element = beehive?.statuses[i];
                if (element.timestamp > dataHandler.nowMinusFrom(from)) {
                    statuses.push(element);
                }
            }
        }

        return statuses.map(function (e) {
            if (with_timestamp) {
                return [e.timestamp, e[type]];
            } else {
                return e[type];
            }
        });
    },
    getTemperatures: (beehive_id) => {
        let beehive = dataHandler.getBeehiveData(beehive_id);

        return beehive?.statuses.map(function (e) {
            return e.temperature;
        });
    },
    getWeights: (beehive_id) => {
        let beehive = dataHandler.getBeehiveData(beehive_id);

        return beehive?.statuses.map(function (e) {
            return e.weight;
        });
    },
    getHumidities: (beehive_id) => {
        let beehive = dataHandler.getBeehiveData(beehive_id);

        return beehive?.statuses.map(function (e) {
            return e.humidity;
        });
    },
    getBatteries: (beehive_id) => {
        let beehive = dataHandler.getBeehiveData(beehive_id);

        return beehive?.statuses.map(function (e) {
            return e.battery;
        });
    },
};
