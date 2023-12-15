/** @type {import('tailwindcss').Config} */

// import colorUtils from "./build_utils/colorUtils";

const colors = require("tailwindcss/colors");

export default {
  content: [
    "./general/**/*.{html,js,svelte,ts}",
    "./dashboard/**/*.{html,js,svelte,ts}",
    "./components/**/*.{html,js,svelte,ts}",
  ],
  safelist: [
    "btn-primary",
    "btn-secondary",
    "btn-tertiary",
    "btn-confirm",
    "btn-error",
    "btn-primary-disabled",
    "btn-secondary-disabled",
    "btn-tertiary-disabled",
    "btn-confirm-disabled",
    "btn-error-disabled",
    "btn-transparent",
    "btn-transparent-white",
    "no-scrollbar",
    "bg-tertiary-300",
  ],
  theme: {
    colors: {
      transparent: "transparent",
      current: "currentColor",
      slate: colors.slate,
      white: "#ffffff",
      red: "#cc0000",
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
  plugins: [
    function ({ addUtilities, theme }) {
      function calcDesaturated(color) {
        let hsl = colorUtils.RGBToHSL(color);

        if (typeof hsl == "object") {
          return colorUtils.hslToString(hsl.h, hsl.s / 2, hsl.l);
        }
        return color;
      }

      const buttonUtilities = {
        ".btn-primary": {
          "background-color": theme("colors.secondary.500"),
          "--icon-color": "black",
          "--text-color": "black",
        },
        ".btn-secondary": {
          "--icon-color": "black",
          "background-color": theme("colors.white"),
          outline: "2px solid #cfd2d6",
        },

        ".btn-confirm": {
          "background-color": theme("colors.confirm.500"),
          "--icon-color": "black",
        },
        ".btn-error": {
          "background-color": theme("colors.error.500"),
          "--text-color": "white",
          "--icon-color": "white",
        },
        ".btn-transparent": {
          "--text-color": theme("colors.primary.900"),
          "--icon-color": "black",
        },
        ".btn-transparent-white": {
          "--text-color": theme("colors.slate.50"),
          "--icon-color": "white",
        },
      };
      addUtilities(buttonUtilities);

      const buttonsDisabled = {
        ".btn-primary-disabled": {
          "background-color": "#A57A0B",
          "--icon-color": "#202325",
          "--text-color": "#202325",
        },
        ".btn-secondary-disabled": {
          "--icon-color": "#202325",
          "background-color": "#A3A5A9",
          outline: "2px solid #cfd2d6",
        },
        ".btn-confirm-disabled": {
          "background-color": "#22c55e",
          "--text-color": "#16a34a",
          "--icon-color": "#16a34a",
        },
        ".btn-error-disabled": {
          "background-color": "#ef4444",
          "--text-color": "#202325",
          "--icon-color": "#202325",
        },
        ".btn-transparent-disabled": {
          "--text-color": "#202325",
          "--icon-color": "#202325",
        },
        ".btn-transparent-white-disabled": {
          "--text-color": "#7e8183",
          "--icon-color": "#7E8183",
        },
      };

      // const fs = require('fs');
      //
      // const jsonData = JSON.stringify(buttonsDisabled, null, 2);
      //
      // fs.writeFile('disabledDataButton.json', jsonData, (err) => {
      //   if (err) {
      //     console.error('Error writing to file:', err);
      //   } else {
      //     console.log('Successfully wrote to data.json');
      //   }
      // });

      addUtilities(buttonsDisabled);

      const scrollbar = {
        ".no-scrollbar::-webkit-scrollbar": {
          display: "none",
        },
        ".no-scrollbar": {
          "-ms-overflow-style": "none",
          "scrollbar-width": "none",
        },
      };
      addUtilities(scrollbar);

      const newUtilities = {
        ".isolation-isolate": {
          isolation: "isolate",
        },
      };
      addUtilities(newUtilities, ["responsive", "hover"]);
    },
  ],
};
