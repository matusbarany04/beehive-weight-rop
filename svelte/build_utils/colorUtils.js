const NodeCache = require("node-cache");
const myCache = new NodeCache();

function hexToDec(s) {
  var i,
    j,
    digits = [0],
    carry;
  for (i = 0; i < s.length; i += 1) {
    carry = parseInt(s.charAt(i), 16);
    for (j = 0; j < digits.length; j += 1) {
      digits[j] = digits[j] * 16 + carry;
      carry = (digits[j] / 10) | 0;
      digits[j] %= 10;
    }
    while (carry > 0) {
      digits.push(carry % 10);
      carry = (carry / 10) | 0;
    }
  }
  return digits.reverse().join("");
}
export default {
  RGBToHSL: function (rgb) {
    let value = myCache.get(rgb);
    if (value === undefined) {
      let ex = /^#([0-9a-fA-F]{2})([0-9a-fA-F]{2})([0-9a-fA-F]{2})$/i;
      if (ex.test(rgb)) {
        const redHex = rgb.slice(1, 3);
        const greenHex = rgb.slice(3, 5);
        const blueHex = rgb.slice(5, 7);

        // make r, g, and b fractions of 1
        let r = hexToDec(redHex) / 255,
          g = hexToDec(greenHex) / 255,
          b = hexToDec(blueHex) / 255,
          // find greatest and smallest channel values
          cmin = Math.min(r, g, b),
          cmax = Math.max(r, g, b),
          delta = cmax - cmin,
          h = 0,
          s = 0,
          l = 0;

        // calculate hue
        // no difference
        if (delta === 0) h = 0;
        // red is max
        else if (cmax === r) h = ((g - b) / delta) % 6;
        // green is max
        else if (cmax === g) h = (b - r) / delta + 2;
        // blue is max
        else h = (r - g) / delta + 4;

        h = Math.round(h * 60);

        // make negative hues positive behind 360°
        if (h < 0) h += 360;

        // calculate lightness
        l = (cmax + cmin) / 2;

        // calculate saturation
        s = delta === 0 ? 0 : delta / (1 - Math.abs(2 * l - 1));

        // multiply l and s by 100
        s = +(s * 100).toFixed(1);
        l = +(l * 100).toFixed(1);

        let output = {
          h: h,
          s: s,
          l: l,
        };
        myCache.set(rgb, output);
        return output;
      } else {
        let error = "Invalid input color";
        myCache.set(rgb, error);
        return error;
      }
    }
  },

  hslToString(h, s, l) {
    return "hsl(" + h + "," + s + "%," + l + "%)";
  },
};
