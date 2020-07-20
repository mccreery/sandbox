import * as units from "./units.js";
import * as inputs from "./inputs.js";
import chunk from "./chunk.js";

window.chunk = chunk;

const input = document.getElementById("input");
const dots = document.getElementById("dots");

let endDate = null;
function startTimer(seconds) {
  endDate = new Date();
  endDate.setTime(endDate.getTime() + seconds * 1000);
}

function stopTimer() {
  endDate = null;
}

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

setInterval(() => {
  if (endDate) {
    const now = new Date();

    let seconds;
    if (now >= endDate) {
      seconds = 0;
    } else {
      seconds = Math.round((endDate - new Date()) / 1000);
    }
    input.value = formatTime(seconds);
  }
}, 50);

function cleanInput() {
  inputs.replace(input, /[^\d]+/g, "");
  dots.value = ".!!".repeat(Math.min(2, (input.value.length - 1) / 2));
}

input.addEventListener("focus", () => {
  stopTimer();
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

document.addEventListener("keydown", e => {
  if (e.key === "Enter") {
    if (document.activeElement === input) {
      input.blur();
      startTimer(inverseFormatTime(input.value));
    } else {
      input.focus();
      input.select();
    }
  }
});

input.focus();
input.select();
