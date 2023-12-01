<script>
  /**
   * @fileoverview Provides testing ground for new components
   * @module Test
   */

  import Button from "../../../components/Buttons/Button.svelte";
  import ButtonSmall from "../../../components/Buttons/ButtonSmall.svelte";
  import CircleButton from "../../../components/Buttons/CircleButton.svelte";
  import message from "../stores/message";
  import { GridResolver } from "../component/cards/utils/gridResolver";
  import panelState from "../component/panel/panelState";
  import { onMount } from "svelte";
  import shared, { onLoad } from "../stores/shared";
  import staticFuncs from "../../../components/lib/utils/staticFuncs";

  message.setMessage("Test");

  const items = [
    {
      x: 0, // X coordinate (column)
      y: 0, // Y coordinate (row)
      w: 1, // Width span
      h: 1, // Height span
    },
    {
      x: 1,
      y: 0,
      w: 1,
      h: 1,
    },
    {
      x: 0,
      y: 1,
      w: 1,
      h: 1,
    },
    {
      x: 3,
      y: 0,
      w: 1,
      h: 1,
    },
  ];

  // Call the isPossible method with a 4x4 grid and the sample items
  const possible = GridResolver.isPossible(4, 4, items);
  console.log("old items", possible);
  GridResolver.gridAsString(4, 4, items);

  let newItems = GridResolver.resolveAroundItem(4, 4, items, {
    x: 1,
    y: 0,
    w: 1,
    h: 2,
  });
  console.log("new items");

  GridResolver.gridAsString(4, 4, [
    ...items,
    {
      x: 1,
      y: 0,
      w: 1,
      h: 2,
    },
  ]);

  /** @type {Array<BeehiveObj>} */
  let beehives = "loading";
  let beehiveId = "c677410a-3593-42f1-b12e-a0e2d1fa6d7a";
  onMount(() => {
    onLoad(["statuses", "beehives"], (statusesData, beehivesData) => {
      beehives = shared.getBeehives();
      console.log(beehives);
    });
  });

  let obj1 = { a: 1, b: 2, c: 3 };
  let obj2 = { ...obj1 };
</script>

<svelte:head>
  <title>Test</title>
  <meta name="Test" content="Test" />
</svelte:head>

<p>obj1</p>
<pre>
  <code>
    {JSON.stringify(obj1, null, 2)}
  </code>
</pre>
<br />

<p>obj2</p>
<pre>
  <code>
    {JSON.stringify(obj2, null, 2)}
  </code>
</pre>

<Button
  onClick={() => {
    obj2["a"] = 5;
  }}
  text="Set to five"
></Button>

<p>a: {obj2.a}</p>

<Button
  onClick={() => {
    obj2 = { ...obj1 }; // staticFuncs.copyKeepReference(obj1, obj2)
  }}
  text="Reset"
></Button>

<br />
<br />
<br />
<br />

{#each Object.values(beehives) as beehive, key}
  <p>{beehive.beehive_id}</p>
  <p>{key}</p>
  <br />
{/each}

<br />
<Button image="icons/android.svg" type="primary" text="android"></Button>
<br />
<ButtonSmall type="primary" text="android"></ButtonSmall>
<br />
<CircleButton image="icons/android.svg" type="primary" text="android"
></CircleButton>
<br />
<Button image="icons/android.svg" text="android" type="secondary"></Button>
<br />
<ButtonSmall image="icons/android.svg" type="secondary" text="android"
></ButtonSmall>
<br />
<CircleButton image="icons/android.svg" type="secondary" text="android"
></CircleButton>
<br />
<Button image="icons/android.svg" text="android" type="tertiary"></Button>
<br />
<ButtonSmall image="icons/android.svg" type="tertiary" text="android"
></ButtonSmall>
<br />
<CircleButton image="icons/android.svg" type="tertiary" text="android"
></CircleButton>
<br />
<Button image="icons/android.svg" type="confirm" text="android"></Button>
<br />
<ButtonSmall image="icons/android.svg" type="confirm" text="android"
></ButtonSmall>
<br />
<CircleButton image="icons/android.svg" type="confirm" text="android"
></CircleButton>
<br />
<Button image="icons/android.svg" type="error" text="android"></Button>
<br />
<ButtonSmall image="icons/android.svg" type="error" text="android"
></ButtonSmall>
<br />
<CircleButton image="icons/android.svg" type="error" text="android"
></CircleButton>
<br />
