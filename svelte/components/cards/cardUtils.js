import PercentageCard from "./PercentageCard.svelte";
import TestCard from "./TestCard.svelte";
import DeletedCard from "./DeletedCard.svelte";
import MapCard from "./MapCard.svelte";
import WeatherCard from "./WeatherCard.svelte";
import ApexChart from "./ApexChart.svelte";
import DoubleGraph from "./DoubleGraph.svelte";
/**
 *  this method will return title, span, position and other attributes of the element
 * @param {*} id of the card
 */
export const getCard = (id) => {};
/**
 *  this method will return data for the card
 * @param {*} id of the card
 */
export const getData = (id) => {};

const cardtypes = [
  // { format: "LineGraph", component: ApexChart },
  // { format: "DoubleGraph", component: DoubleGraph },
  // { format: "PercentageCard", component: PercentageCard },
  // { format: "MapCard", component: MapCard },
  // { format: "WeatherCard", component: WeatherCard },
  // { format: "TestCard", component: TestCard },
];

export function getCardTypes() {
  return cardtypes;
}

export function getCardByFormat(format) {
  let output = cardtypes.find((type) => type.format == format);
  if (!output) {
    return DeletedCard;
  }
  return output.component;
}

