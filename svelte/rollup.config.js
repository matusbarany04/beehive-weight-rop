import svelte from "rollup-plugin-svelte"; 
import resolve from "@rollup/plugin-node-resolve";
import commonjs from "@rollup/plugin-commonjs";
import terser from "@rollup/plugin-terser";
import postcss from "rollup-plugin-postcss";
import tailwindcss from "tailwindcss";
import livereload from "rollup-plugin-livereload";

const production = !process.env.ROLLUP_WATCH;

const commonPlugins = [
  postcss({
    extract: false,
    plugins: [tailwindcss()],
    use: [["sass", { includePaths: ["./src/styles", "./node_modules"] }]],
  }),
  svelte({
    dev: !production,
  }),
  resolve(),
  commonjs(),
  production && terser(),
];

const dashboardConfig = {
  input: "dashboard/main.js",
  output: {
    sourcemap: !production,
    format: "iife",
    name: "app",
    file: "../src/main/resources/bundle/bundle.js",
    treeshake: true,
  },
  plugins: [...commonPlugins, !production && livereload("public")],
  // external: ['name-of-large-dependency'],  // Uncomment and list any large external dependencies here
};

const indexConfig = {
  input: "general/main.js",
  output: {
    sourcemap: !production,
    format: "iife",
    name: "app",
    file: "../src/main/resources/bundle/indexBundle.js",
    treeshake: true,
  },
  plugins: commonPlugins,
  // external: ['name-of-large-dependency'],  // Uncomment and list any large external dependencies here
};

export default [dashboardConfig, indexConfig];