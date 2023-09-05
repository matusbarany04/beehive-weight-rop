import { readable } from "svelte/store";

export default function (value, options = {}) {
  let interval = null;
  let end = -1;
  // function to compute the next value
  const nextVal = () => {
    // if current time is past end date, return null
    if (0 >= value - end) {
      interval ?? clearInterval(interval);
      return null;
    } 
    end += 1;
    // compute interval between start & end using the date-fns library
    return value - end;
  };

  return readable(nextVal(), (set) => {
    // the update function sets the latest date
    const update = () => set(nextVal());

    // setup an interval timer to update the store's value repeatedly over time
    interval = setInterval(update, options.interval || 1000);

    // return unsubscribe callback:
    // it will stop the timer when the store is destroyed
    return () => interval ?? clearInterval(interval);
  });
}
