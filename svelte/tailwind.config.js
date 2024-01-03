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
    "btn-secondary-noborder",
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
    "bg-confirm-100",
    "bg-confirm-200",
    "bg-confirm-300",
    "bg-confirm-400",
    "bg-confirm-500",
    "text-confirm-100",
    "text-confirm-200",
    "text-confirm-300",
    "text-confirm-400",
    "text-confirm-500",
    "waving-hand",
    "wobble",
  ],
  theme: {
    colors: {
      transparent: "transparent",
      current: "currentColor",
      slate: colors.slate,
      white: "#ffffff",
      red: "#cc0000",
      ...colors,
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
    extend: {
      aspectRatio: {
        "4/3": "4 / 3",
        "5/3": "5 / 3",
        "2/1": "2 / 1",
        "video-vertical": "9 / 16",
      },
      fontSize: {
        "10xl": "9rem" /* Adjust as needed */,
        "11xl": "10rem" /* Adjust as needed */,
        "12xl": "12rem" /* Adjust as needed */,
      },
      lineHeight: {
        1: "1rem",
        1.25: "1.25rem",
        1.5: "1.5rem",
        1.75: "1.75rem",
        2: "2rem",
        2.25: "2.25rem",
        2.5: "2.5rem",
        2.75: "2.75rem",
        3: "3rem",
      },
      keyframes: {
        wobble: {
          "0%, 100%": {
            transform: "translateX(0%)",
            transformOrigin: "50% 50%",
          },
          "15%": {
            transform: "translateX(-30px) rotate(-6deg)",
          },
          "30%": {
            transform: "translateX(15px) rotate(6deg)",
          },
          "45%": {
            transform: "translateX(-15px) rotate(-3.6deg)",
          },
          "60%": {
            transform: "translateX(9px) rotate(2.4deg)",
          },
          "75%": {
            transform: "translateX(-6px) rotate(-1.2deg)",
          },
        },
        wave: {
          "0%": {transform: "rotate(0.0deg)"},
          "10%": {transform: "rotate(14deg)"},
          "20%": {transform: "rotate(-8deg)"},
          "30%": {transform: "rotate(14deg)"},
          "40%": {transform: "rotate(-4deg)"},
          "50%": {transform: "rotate(10.0deg)"},
          "60%": {transform: "rotate(0.0deg)"},
          "100%": {transform: "rotate(0.0deg)"},
        },

        "focus-in-expand-fwd": {
          "0%": {
            "letter-spacing": "-0.5em",
            transform: "translateZ(-800px)",
            filter: "blur(12px)",
            opacity: "0",
          },
          "100%": {
            transform: "translateZ(0)",
            filter: "blur(0)",
            opacity: "1",
          },
        },
        "text-focus-in": {
          "0%": {
            filter: "blur(12px)",
            opacity: 0,
          },
          "100%": {
            filter: "blur(0px)",
            opacity: 1,
          },
        },
        "slide-in-top": {
          "0%": {
            "-webkit-transform": "translateY(-1000px)",
            "transform": "translateY(-1000px)",
            "opacity": "0"
          },
          "100%": {
            "-webkit-transform": "translateY(0)",
            "transform": "translateY(0)",
            "opacity": "1"
          }
        },
      },
      animation: {
        // ...animations,
        wobble: "wobble 0.8s both",
        "waving-hand": "wave 2s linear infinite",
        "focus-in-expand-fwd":
          "focus-in-expand-fwd 0.8s cubic-bezier(0.250, 0.460, 0.450, 0.940) both",
        "text-focus-in":
          "text-focus-in 1s cubic-bezier(0.550, 0.085, 0.680, 0.530) both",
        "slide-in-top": "slide-in-top 0.5s cubic-bezier(0.250, 0.460, 0.450, 0.940) both"
      },
    },
  },
  plugins: [
    function ({addUtilities, theme}) {
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
        ".btn-secondary-noborder": {
          "--icon-color": "black",
          "border-radius": "10px",
          "background-color": theme("colors.white"),
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

      const swipeButton = Object.keys(theme("backgroundColor")).reduce(
        (acc, color) => {
          const baseClass = `.btn-swipe-${color}`;
          const altClass = `.btn-swipe-${color}-alt`;
          const hoverClass = `.btn-swipe-${color}:hover`;
          const hoverAltClass = `.btn-swipe-${color}-alt:hover`;

          acc[baseClass] = {
            background: `linear-gradient(${theme(
              `backgroundColor.${color}`,
            )} 0 0) var(--p,0%) / var(--p,0%) no-repeat`,
            transition: "background .4s, background-position 0s",
          };

          acc[altClass] = {
            background: `linear-gradient(${theme(
              `backgroundColor.${color}`,
            )} 0 0) calc(100% - var(--p,0%)) / var(--p,0%) no-repeat`,
            transition: "background .4s, background-position 0s",
          };

          acc[hoverClass] = {
            "--p": "100%",
          };

          acc[hoverAltClass] = {
            "--p": "100%",
          };

          return acc;
        },
        {},
      );

      addUtilities(swipeButton);
    },
  ],
};
