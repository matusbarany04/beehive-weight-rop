<script>
  /**
   *
   * This page displays a calendar that hold important events for beekeepers
   *
   * @class Calendar
   * @constructor
   */
  
  import DayItem from "../../../components/calendar/DayItem.svelte";
  import Button from "../../../components/Buttons/Button.svelte";
  import Modal from "../../../components/Modal.svelte";
  import Input from "../../../components/Inputs/Input.svelte";
  import DropdownInput from "../../../components/Inputs/DropdownInput.svelte";

  let daysOfMonth = [];
  let now = new Date();
  now.setHours(0, 0, 0, 0);
  let markedItem = now;
  let newReminder = false;
  let reminders = [];

  updateCalendar();

  function updateReminders() {
    fetch("/dashboardApi/getReminders")
      .then((r) => r.json())
      .then((response) => {
        reminders = response["reminders"];
        showReminders();
      });
  }

  function showReminders() {
    for (let reminder of reminders) {
      const date = new Date(reminder.date);
      const index = getDayIndex(
        new Date(date.getFullYear(), date.getMonth(), date.getDate()),
      );
      if (index !== -1) daysOfMonth[index].reminders.push(reminder);
      daysOfMonth = daysOfMonth;
    }
  }

  function getDayIndex(day) {
    for (let i = 0; i < daysOfMonth.length; i++) {
      if (daysOfMonth[i].date.getTime() === day.getTime()) return i;
    }

    return -1;
  }

  function nextMonth() {
    now.setMonth(now.getMonth() + 1);
    updateCalendar();
  }

  function previousMonth() {
    now.setMonth(now.getMonth() - 1);
    updateCalendar();
  }

  function updateCalendar() {
    now = now;
    let firstDay = new Date(now.getFullYear(), now.getMonth(), 1);
    let lastDay = new Date(now.getFullYear(), now.getMonth() + 1, 0);

    firstDay.setDate(firstDay.getDate() - (firstDay.getDay() || 7) + 1);
    lastDay.setDate(lastDay.getDate() + 7 - (lastDay.getDay() || 7));

    let i, d;
    daysOfMonth.splice(0);

    for (d = firstDay, i = 0; d <= lastDay; d.setDate(d.getDate() + 1), i++) {
      daysOfMonth.push({ date: new Date(d), reminders: [] });
      console.log(d);
    }

    updateReminders();
  }

  function saveReminder(e) {
    let formData = new FormData(e.target);
    fetch("/dashboardApi/newReminder", { method: "POST", body: formData })
      .then((r) => r.json())
      .then((response) => {
        newReminder = false;
        if (response.status === "ok") {
          updateCalendar();
        }
      });
  }

  function calcTextColor(background) {
    const match = background.match(
      /^#?([0-9a-f]{2})([0-9a-f]{2})([0-9a-f]{2})$/i,
    );
    const rgb = match.slice(1).map((h) => parseInt(h, 16));
    if (rgb[0] + rgb[1] * 2 < 380) return "white";
    else return "black";
  }
</script>

<div class="flex h-4/5 flex-col" on:load={onload}>
  <div>
    <div class="flex items-center justify-end">
      <h2 class="w-full" id="title">
        {markedItem.toLocaleDateString("default", {
          year: "numeric",
          month: "long",
          weekday: "long",
          day: "numeric",
        })}
      </h2>

      <button on:click={previousMonth} class="cursor-pointer">
        <img alt="previous-month" src="../../icons/arrow-left.svg" />
      </button>

      <h2 class="w-48 text-center">
        {now.toLocaleString("default", { month: "long", year: "numeric" })}
      </h2>

      <button on:click={nextMonth} class="cursor-pointer">
        <img alt="previous-month" src="../../icons/arrow-right.svg" />
      </button>
    </div>
    <Button
      image="../../icons/add_thin.svg"
      text="Nová poznámka/primomienka"
      type="primary"
      onClick={() => (newReminder = true)}
    />

    <div class="flex items-center pt-5">
      <h3 class="w-full p-2">PO</h3>
      <h3 class="w-full p-2">UT</h3>
      <h3 class="w-full p-2">ST</h3>
      <h3 class="w-full p-2">ŠT</h3>
      <h3 class="w-full p-2">PI</h3>
      <h3 class="w-full p-2">SO</h3>
      <h3 class="w-full p-2">NE</h3>
    </div>
  </div>

  <div class="grid h-full auto-rows-fr grid-cols-7 gap-1">
    {#each daysOfMonth as day}
      <DayItem
        date={day.date}
        active={day.date.getMonth() === now.getMonth()}
        marked={markedItem.getTime() === day.date.getTime()}
        on:mousedown={() => (markedItem = day.date)}
      >
        {#each day.reminders as reminder}
          <div
            style="background-color: {reminder.color}"
            class="rounded p-1 text-{calcTextColor(reminder.color)}"
          >
            {reminder.title} - {reminder.time}
          </div>
        {/each}
      </DayItem>
    {/each}
  </div>
</div>

<Modal bind:showModal={newReminder}>
  <h2 slot="header" class="text-2xl font-bold">
    {"Nová poznámka/primomienka"}
  </h2>

  <form
    id="newReminder"
    on:submit|preventDefault={saveReminder}
    class="my-4 flex flex-col gap-4"
  >
    <Input
      label="Názov"
      placeholder="Názov"
      name="title"
      value=""
      required="required"
    />

    <Input label="Poznámka" placeholder="Poznámka" name="details" value="" />

    <label for="color">Farba: </label>
    <input type="color" name="color" id="color" value="#0000ff" />

    <label for="date">Dátum: </label>
    <input
      class="h-8 w-44 rounded-md border-2 border-slate-300 px-4"
      type="date"
      id="date"
      name="date"
      value={new Date(
        markedItem.getTime() - markedItem.getTimezoneOffset() * 60000,
      )
        .toISOString()
        .split("T")[0]}
      pattern="[0-2][0-9]:[0-5][0-9]"
      required
    />

    <label for="time">Čas: </label>
    <input
      class="h-8 w-44 rounded-md border-2 border-slate-300 px-4"
      type="time"
      id="time"
      name="time"
      min="00:00"
      max="23:59"
      pattern="[0-2][0-9]:[0-5][0-9]"
      required
    />

    <DropdownInput
      label="Upozornenie"
      name="notifyBy"
      options={[
        [0, "-"], //TODO opravit hodnoty
        [1, "Email"],
      ]}
    />
  </form>
  <Button
    formId="newReminder"
    slot="footer"
    type="confirm"
    autofocus
    clickType="submit"
    text="Uložiť a zatvoriť okno"
  />
</Modal>
