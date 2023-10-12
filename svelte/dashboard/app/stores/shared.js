/**
 * @fileoverview Provides utilities for sharing dashboard data between across the whole dashboard
 * @module shared.js
 */

import {get, writable} from "svelte/store";
import {BeehiveObj} from "./Beehive";

const user = writable({});

export let dataTypes = ["temperature", "humidity", "weight"];
export let fromValues = ["all", "hour", "day", "week", "month", "year"];
export let topValues = ["now"];

const beehiveList = {}

export default {
    /**
     nacita pouzivatelove data zo servera
     */
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
            for (const beehiveJson of response["beehives"]) {
                beehiveList[beehiveJson["token"]] = new BeehiveObj(
                    beehiveJson["token"],
                    beehiveJson["name"],
                    beehiveJson["location"],
                    beehiveJson["model"],
                    beehiveJson["connectionMode"],
                    beehiveJson["interval"],
                    beehiveJson["devices"],
                );
            }

            loadData("beehives", response["beehives"]);
        }
    },
    /**
     * Loads data of beehives, call after loading beehives
     */
    fetchStatuses: function () {
        fetch("/dashboardApi/getData")
            .then((r) => r.json())
            .then((response) => {
                //  beehive_data.set(response);
                onLoad("beehives", (beehives) => {
                    // Parsing the raw data into objects created in fetchBeehives
                    for (const beehive_id in response.data) {
                        const beehiveData = response.data[beehive_id]
                        for (const key in beehiveData) {
                            if (Array.isArray(beehiveData[key])) {
                                /** @type{BeehiveObj} */
                                let beehiveObj = beehiveList[beehive_id]
                                // check if beehive has any data posted
                                if(beehiveObj != null){
                                    // if so load data to beehive object 
                                    beehiveObj.setData(key, beehiveData[key])
                                }
                            }
                        }
                    }
                })
                loadData("statuses", response.data);
            });
    },
    // possible buggy buggy
    getUser: function () {
        let userValue;
        user.subscribe((value) => {
            userValue = value;
        })();
        return userValue;
    },
    getBeehives: function () {
        return beehiveList;
    },
    /**
     * 
     * @param beehive_id
     * @return {BeehiveObj}
     */
    getBeehiveById: function (beehive_id) {
        return this.getBeehives()[beehive_id];
    },
    /* might not work properly */
    getBeehiveIdsWithNames: function () {
        return savedData["beehives"].map((object) => {
            return [object.token, object.name];
        });
    },
    nowMinusFrom: function (from) {
        return new Date(new Date().getTime() - fromValueToTimestamp(from));
    },
};

let savedData = {};
let callbacks = [];

/**
 * callback po nacitani vybratych dat
 * @param dataTypes vyber dat (array)
 * @param callback funkcia po nacitani (argumenty v poradi ako vyber dat)
 */
export function onLoad(dataTypes, callback) {
    if (!Array.isArray(dataTypes)) dataTypes = [dataTypes];

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

/**
 * Function distributes provided data to all subscribed parties
 *
 * @param dataType name of data
 * @param data data itself
 */
export function loadData(dataType, data) {
    // TODO spread and assign if object?
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
