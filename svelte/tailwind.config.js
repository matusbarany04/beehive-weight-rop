/** @type {import('tailwindcss').Config} */

const colors = require("tailwindcss/colors");

export default {
  content: ["./src/**/*.{html,js,svelte,ts}"],
  theme: {
    colors: {
      transparent: "transparent",
      current: "currentColor",
      slate: colors.slate,
      white: "#ffffff",
      primary: {
        100: "#202325",
        200: "#1D2021",
        300: "#1B1D1E",
        400: "#181A1C",
        500: "#161819",
        600: "#141617",
        700: "#121314",
        800: "#101112",
        900: "#0E1010",
      },
      secondary: {
        100: "#FFF516",
        200: "#FFDE14",
        300: "#FFCA12",
        400: "#F9B811",
        500: "#e2a70f",
        600: "#CB960E",
        700: "#B7870C",
        800: "#A57A0B",
        900: "#946E0A",
      },
      tertiary: {
        100: "#E0E3E8",
        200: "#CACCD1",
        300: "#B5B8BC",
        400: "#A3A5A9",
        500: "#939598",
      },
      confirm: colors.green,
      error: colors.red,
      // ...
    },
    extend: {},
  },
  plugins: [],
};
