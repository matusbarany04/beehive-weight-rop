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
export let beehivesLoaded = false;



export const dataHandler = {
  
  
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
  
};
