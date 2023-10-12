import shared from "./shared";

export class BeehiveObj {
  /** @type {string} Unique identifier for the beehive. */
  beehive_id;

  /** @type {string} Location of the beehive. */
  location;

  /** @type {string} Model of the beehive. */
  model;

  /** @type {number} Connection mode of the beehive. */
  connectionMode;

  /** @type {number} Time interval for data collection. */
  interval;

  /** @type {Array<{name: string, port: string, id: number, type: string}>} Devices associated with the beehive. */
  devices;

  /** @type {Object<Array>} stores all of the beehive data */
  data = {};

  /**
   * Creates a new Beehive instance.
   * @param {string} beehive_id Unique identifier for the beehive.
   * @param name {string}
   * @param {string} location Location of the beehive.
   * @param {string} model Model of the beehive.
   * @param {number} connectionMode Connection mode of the beehive.
   * @param {number} interval Time interval for data collection.
   * @param {Array<{name: string, port: string, id: number, type: string}>} devices Devices associated with the beehive.
   */
  constructor(
    beehive_id,
    name,
    location,
    model,
    connectionMode,
    interval,
    devices,
  ) {
    if (!beehive_id)
      throw new Error("Beehive ID is required. provided - " + beehive_id);
    if (location == null)
      throw new Error("Location is required. provided - " + location);
    if (!model) throw new Error("Model is required.");
    if (connectionMode === null || connectionMode === undefined)
      throw new Error("Connection mode is required.");
    if (interval === null || interval === undefined)
      throw new Error("Interval is required.");
    if (!Array.isArray(devices)) throw new Error("Devices are required.");

    this.beehive_id = beehive_id;
    this.name = name;
    this.location = location;
    this.model = model;
    this.connectionMode = connectionMode;
    this.interval = interval;
    this.devices = devices;
    this.data = {
      weight: [],
      timestamp: [],
    };
  }

  /**
   * Sets data for a specific type. If the type doesn't exist, it creates one.
   * @param {string} type - The type of data.
   * @param {Array} data - The data to be set.
   */
  setData(type, data) {
    if (!Array.isArray(data)) {
      console.warn(
        "Data provided is not an array. Setting it to an empty array.",
      );
      this.data[type] = [];
    } else {
      this.data[type] = data;
    }
  }

  /**
   * Sets multiple data types and their respective data.
   * @param {Array<string>} types - Array of types of data.
   * @param {Array<Array>} dataArrays - Array of data arrays.
   */
  setMultipleData(types, dataArrays) {
    if (!Array.isArray(types) || !Array.isArray(dataArrays)) {
      console.warn("Both types and dataArrays should be arrays.");
      return;
    }

    if (types.length !== dataArrays.length) {
      console.warn("Length of types and dataArrays should be the same.");
      return;
    }

    for (let i = 0; i < types.length; i++) {
      this.setData(types[i], dataArrays[i]);
    }
  }

  /**
   * Retrieves data based on the given type.
   * @param {string} type - The type of data ('temperature', 'humidity', 'weight', or 'timestamp').
   * @returns {Array|[]} - An array containing data for the specified type or an empty array if type is invalid.
   */
  getAllDataByType(type) {
    if (this.data.hasOwnProperty(type)) {
      return this.data[type];
    } else {
      console.warn("Invalid data type requested.");
      return []; // Return empty array for invalid type.
    }
  }

  /**
   * Retrieves last data based on the given type.
   * @param {string} type - The type of data ('temperature', 'humidity', 'weight', or 'timestamp').
   * @returns {string|number} - last value from an array
   */
  getLastDataByType(type) {
    let data = this.getAllDataByType(type);
    return data[data.length - 1];
  }

  /**
   * Returns beehive timestamps array
   * @return {[]}
   */
  getTimestamps() {
    return this.data["timestamp"];
  }

  /**
   * Returns beehive loadedData state
   * @return {boolean}
   */
  hasData() {
    return this.getTimestamps().length > 0;
  }

  /**
   *
   * @param type {string}
   * @param beehive_id {string}
   * @param with_timestamp {boolean}
   * @param from {string}
   * @return {[*,*][]}
   */
  getDataByType(type, with_timestamp, from = 0) {
    if (this.hasData()) {
      let statuses = this.getAllDataByType(type);
      let timestamps = this.getTimestamps();

      let out = timestamps.map((e, i) => [e, statuses[i]]);

      if (from != null) {
        out = [];
        for (let i = 0; i < statuses.length; i++) {
          if (timestamps[i] > shared.nowMinusFrom(from)) {
            if (with_timestamp) {
              out.push([timestamps[i], statuses[i]]);
            } else {
              out.push(statuses[i]);
            }
          }
        }
      }
      return out;
    } else {
      return [];
    }
  }

  /**
   * @return {Array|[]}
   */
  getWeight() {
    return this.getAllDataByType("weight");
  }

  /**
   * Returns the most recent timestamp.
   * @returns {number|null} - The last timestamp or null if no timestamps available.
   */
  getLastUpdateTime() {
    const timestamps = this.getAllDataByType("timestamp");
    return timestamps[timestamps.length - 1];
  }

  getBattery() {
    const battery = this.getAllDataByType("battery");
    return battery[battery.length - 1];
  }
}
