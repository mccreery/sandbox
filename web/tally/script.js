"use strict";

let numTallies = 0;
const fontLoad = document.fonts && document.fonts.load("italic bold 1em DSEG7");

function enableButton(button, action) {
  button.onclick = action;
  button.disabled = false;
}

function initTally(tally) {
  tally.querySelector(".tally-name").value = "Tally " + ++numTallies;

  const counter = tally.querySelector(".tally-box");
  // Only hide arrows when JS is enabled
  counter.classList.add("plain-number");

  enableButton(tally.querySelector(".minus"), () => --counter.value);
  enableButton(tally.querySelector(".plus"), () => ++counter.value);
  enableButton(tally.querySelector(".reset"), () => counter.value = 0);

  enableButton(tally.querySelector(".close-button"), () => tally.parentNode.removeChild(tally));

  fontLoad && !document.querySelector(".lcd-effect") && fontLoad.then(() => {
    const lcdEffect = document.createElement("span");
    lcdEffect.className = "lcd-effect";
    tally.appendChild(lcdEffect);
  });

  return tally;
}

const template = document.querySelector(".tally");
initTally(template);

enableButton(document.querySelector("#create"), function() {
  const clone = template.cloneNode(true);
  initTally(clone);
  this.parentNode.insertBefore(clone, this);
});
