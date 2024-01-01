<script>
  import { onMount } from "svelte/internal";
  import {
    createProgramFromSources,
    bindQuadBuffer,
  } from "../shader/ShaderUtils.svelte";

  // Default fragment shader
  export let fragmentShader = `
		precision highp float;

		uniform vec2 u_resolution;
		uniform vec2 u_mouse;
		uniform float u_time;

		void main() {
			gl_FragColor = vec4(fract((gl_FragCoord.xy - u_mouse) / u_resolution), fract(u_time), 1);
		}
	`;

  export let scale = 1;

  let canvas;

  let m = { x: 0, y: 0 };
  let time = 0;

  function handleMousemove(event) {
    m.x = event.clientX * scale;
    m.y = event.clientY * scale;
  }

  onMount(() => {
    console.log("dfdsfsd")
    const gl = canvas.getContext("webgl");
    let frame;

    if (!gl) {
      // Handle cases for no WebGL support
      console.log("WebGL needed in order to run shader demo.");
      return;
    }

    // Default vertex shader
    const vs = `
			// an attribute will receive data from a buffer
			attribute vec4 a_position;

			// all shaders have a main function
			void main() {

			// gl_Position is a special variable a vertex shader
			// is responsible for setting
			gl_Position = a_position;
			}
		`;

    // Setup GLSL program
    const program = createProgramFromSources(gl, [vs, fragmentShader]);

    // Look up where the vertex data needs to go.
    const positionAttributeLocation = gl.getAttribLocation(
      program,
      "a_position",
    );

    // Look up uniform locations
    const resolutionLocation = gl.getUniformLocation(program, "u_resolution");
    const mouseLocation = gl.getUniformLocation(program, "u_mouse");
    const timeLocation = gl.getUniformLocation(program, "u_time");

    bindQuadBuffer(gl, program, positionAttributeLocation);

    (function loop() {
      frame = requestAnimationFrame(loop);
      time += 0.01;

      gl.uniform2f(resolutionLocation, gl.canvas.width, gl.canvas.height);
      gl.uniform2f(mouseLocation, m.x, -m.y);
      gl.uniform1f(timeLocation, time);

      gl.drawArrays(
        gl.TRIANGLES,
        0, // offset
        6, // num vertices to process
      );
    })();

    return () => {
      cancelAnimationFrame(frame);
    };
  });
</script>

<canvas
  on:mousemove={handleMousemove}
  bind:this={canvas}
  width={window.innerWidth * scale}
  height={window.innerHeight * scale}
></canvas>

<style>
  canvas {
    width: 100%;
    height: 100%;
    background-color: #333;
  }
</style>
