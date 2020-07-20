import { lerp, inverseLerp, clamp } from "./math.js";

export function splice(input, index, length, replacement) {
  const mapIndex = i => {
    let t = inverseLerp(i, index, index + length);
    t = clamp(t, 0, 1);

    i = lerp(index, index + replacement.length, t);
    i = Math.round(i);
    return i;
  };

  const selectionStart = mapIndex(input.selectionStart);
  const selectionEnd = mapIndex(input.selectionEnd);

  input.value = input.value.slice(0, index) + replacement + input.value.slice(index + length);
  input.selectionStart = selectionStart;
  input.selectionEnd = selectionEnd;
}

export function replace(input, regexp, replacement) {
  if (regexp.global || regexp.sticky) {
    let match;
    while ((match = regexp.exec(input.value)) !== null) {
      splice(input, match.index, match[0].length, replacement);
      regexp.lastIndex = match.index + replacement.length;
    }
  } else {
    let match = regexp.exec(input.value);
    if (match !== null) {
      splice(input, match.index, match[0].length, replacement);
    }
  }
}
