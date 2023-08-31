import svelte from 'rollup-plugin-svelte';
import resolve from '@rollup/plugin-node-resolve';  // Updated import
import commonjs from '@rollup/plugin-commonjs';     // Updated import
import terser  from '@rollup/plugin-terser';
import css from 'rollup-plugin-css-only';
const production = !process.env.ROLLUP_WATCH;

export default {
  input: 'src/main.js',
  output: {
    sourcemap: true,
    format: 'iife',
    name: 'app',
    file: '../src/main/resources/static/bundle.js'
  },
  plugins: [
    svelte({
      // enable run-time checks when not in production
      dev: !production,
    }),
    css({ input:"../src/main/resources/static/bundle.css", output: 'bundle.css' }),
    resolve(),
    commonjs(),

    // If we're building for production (npm run build
    // instead of npm run dev), minify
    production && terser()
  ]
};