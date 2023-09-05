import svelte from 'rollup-plugin-svelte';
import resolve from '@rollup/plugin-node-resolve';  // Updated import
import commonjs from '@rollup/plugin-commonjs';     // Updated import
import terser  from '@rollup/plugin-terser';
import css from 'rollup-plugin-css-only';
import postcss from 'rollup-plugin-postcss'
import tailwindcss from 'tailwindcss';
import livereload from 'rollup-plugin-livereload';
import autoprefixer from "autoprefixer";
const production = !process.env.ROLLUP_WATCH;

const dashboardConfig =  {
  input: 'dashboard/main.js',
  output: {
    sourcemap: true,
    format: 'iife',
    name: 'app',
    file: '../src/main/resources/bundle/bundle.js'
  },
  plugins: [
    postcss({
      extract: false, // Inline the CSS into the JS
      plugins: [
        tailwindcss(),
      ],
      use: [
        ['sass', { includePaths: ['./src/styles', './node_modules'] }]
      ]
    }),
    svelte({
      // enable run-time checks when not in production
      dev: !production,
    }),
    // css({ input:"../src/main/resources/static/bundle.css", output: 'bundle.css' }),
    resolve({
      exportConditions: ['browser']
    }),
    commonjs(),
    !production && livereload('public'),
    // If we're building for production (npm run build
    // instead of npm run dev), minify
    production && terser()
  ]
};

const indexConfig =  {
  input: 'general/main.js',
  output: {
    sourcemap: true,
    format: 'iife',
    name: 'app',
    file: '../src/main/resources/bundle/indexBundle.js'
  },
  plugins: [
    postcss({
      extract: false, 
      plugins: [
        tailwindcss()
      ],
      use: [
        ['sass', { includePaths: ['./src/styles', './node_modules'] }]
      ]
    }),
    svelte({
      dev: !production,
    }),
    resolve(),
    commonjs(),

    production && terser()
  ]
};

export default [dashboardConfig, indexConfig];