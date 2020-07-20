import * as timer from "./timer.js";
import * as units from "./units.js";
import * as inputs from "./inputs.js";
import chunk from "./chunk.js";

const input = document.getElementById("input");
const dots = document.getElementById("dots");

function formatTime(seconds) {
  let groups = units.group(seconds, [60, 60]);
  while (groups.length < 2) {
    groups.push(0);
  }
  groups = groups.reverse();

  for (let i = 1; i < groups.length; i++) {
    groups[i] = groups[i].toString().padStart(2, "0");
  }
  return groups.join(":");
}

function inverseFormatTime(time) {
  const groups = time.split(":").map(x => parseInt(x)).reverse();
  return units.ungroup(groups, [60, 60]);
}

function cleanInput() {
  inputs.replace(input, /[^\d]+/g, "");
  dots.value = ".!!".repeat(Math.min(2, (input.value.length - 1) / 2));
}

input.addEventListener("focus", () => {
  timer.stop();
  cleanInput();
});
input.addEventListener("input", cleanInput);

input.addEventListener("blur", () => {
  const groups = chunk(input.value, 2, {
    reverse: true, max: 2, tail: true
  }).map(x => parseInt(x));

  const seconds = units.ungroup(groups, [60, 60]);
  input.value = formatTime(seconds);
});

input.addEventListener("keydown", e => {
  if (e.key === "Enter") {
    e.stopPropagation();
    input.blur();
    timer.start(inverseFormatTime(input.value));
  }
});

document.addEventListener("keydown", e => {
  if (e.key === "Enter") {
    input.focus();
    input.select();
  } else if (e.key === " ") {
    if (timer.running()) {
      timer.stop();
    } else {
      timer.start();
    }
  }
});

document.addEventListener("timer", e => {
  input.value = formatTime(e.detail);
});

input.focus();
input.select();
