import * as units from "./units.js";
import * as inputs from "./inputs.js";
import chunk from "./chunk.js";

window.chunk = chunk;

const input = document.getElementById("input");
const dots = document.getElementById("dots");
let endDate = null;

function cleanInput() {
  inputs.replace(input, /[^\d]+/g, "");
  dots.value = ".!!".repeat(Math.min(2, (input.value.length - 1) / 2));
}

input.addEventListener("focus", () => {
  endDate = null;
  cleanInput();
});

input.addEventListener("input", cleanInput);

input.addEventListener("blur", () => {
  const groups = chunk(input.value, 2, {
    reverse: true, max: 2, tail: true
  }).map(x => parseInt(x));

  const seconds = units.ungroup(groups, [60, 60]);
  endDate = new Date();
  endDate.setTime(endDate.getTime() + seconds * 1000);
});

setInterval(() => {
  if (endDate) {
    const now = new Date();

    let seconds;
    if (now >= endDate) {
      seconds = 0;
    } else {
      seconds = Math.round((endDate - new Date()) / 1000);
    }

    let groups = units.group(seconds, [60, 60]);
    while (groups.length < 2) {
      groups.push(0);
    }
    groups = groups.reverse();

    for (let i = 1; i < groups.length; i++) {
      groups[i] = groups[i].toString().padStart(2, "0");
    }
    input.value = groups.join(":");
  }
}, 50);

document.addEventListener("keydown", e => {
  if (e.key === "Enter") {
    if (document.activeElement === input) {
      input.blur();
    } else {
      input.focus();
      input.select();
    }
  }
});
