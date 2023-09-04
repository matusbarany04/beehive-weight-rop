/**
 * @param {array} modes Modes that card can be in 
 */
export const modes = ["default", "add", "edit", "static"];

/**
 * Returns the default mode called 'default' 
 * @function 
 */
export function getDefaultMode() {
  return modes[0];
}

//TODO to be deprecated
const labels = ["January", "February", "March", "April", "May", "June", "July"];

//TODO to be deprecated
export const data = {
  labels,
  datasets: [
    {
      type: "line",
      label: "Dataset 1",
      borderColor: "rgb(255, 99, 132)",
      borderWidth: 2,
      fill: false,
      data: labels.map(() => Math.random() * 1000),
    },
    {
      type: "bar",
      label: "Dataset 2",
      backgroundColor: "rgb(75, 192, 192)",
      data: labels.map(() => Math.random() * 1000),
      borderColor: "white",
      borderWidth: 2,
    },
    {
      type: "bar",
      label: "Dataset 3",
      backgroundColor: "rgb(53, 162, 235)",
      data: labels.map(() => Math.random() * 1000),
    },
  ],
};

