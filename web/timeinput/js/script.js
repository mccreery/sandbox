import * as units from "./units.js";
import * as inputs from "./inputs.js";
import chunk from "./chunk.js";

const input = document.getElementById("input");
const dots = document.getElementById("dots");

function updateDots() {
  // dot then two "all off" characters
  dots.value = ".!!".repeat(Math.min(2, (input.value.length - 1) / 2));
}

input.addEventListener("input", () => {
  inputs.replace(input, /[^\d]+/g, "");
  updateDots();
});

input.addEventListener("focus", () => {
  stopTimer();
  input.selectionStart = 0;
  input.selectionEnd = input.value.length;
  inputs.replace(input, /[^\d]+/g, "");
  updateDots();
  dots.classList.add("show");
});

input.addEventListener("blur", () => {
  dots.classList.remove("show");

  input.value = chunk(input.value, 2, {
    reverse: true, max: 2, tail: true
  }).reverse().join(":");
});

let endDate = null;
function startTimer() {
  const groups = input.value.split(":").reverse().map(x => parseInt(x) || 0);
  const seconds = units.ungroup(groups, [60, 60]);

  endDate = new Date();
  endDate.setTime(endDate.getTime() + seconds * 1000);
}

function stopTimer() {
  endDate = null;
  expired = false;
}

let expired = false;
setInterval(() => {
  if (endDate) {
    const now = new Date();
    expired = now >= endDate;

    let seconds;
    if (expired) {
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

input.addEventListener("keydown", e => {
  if (e.key === "Enter") {
    e.stopPropagation();
    input.blur();
    startTimer();
  }
})

document.addEventListener("keydown", e => {
  if (e.key === " ") {
    if (endDate) {
      stopTimer();
    } else {
      startTimer();
    }
  } else if (e.key === "Enter") {
    input.focus();
  }
});
