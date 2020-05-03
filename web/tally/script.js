"use strict";

let numTallies = 0;
const fontLoad = document.fonts.load("italic bold 1em DSEG7");

function addTally(initialValue) {
  initialValue = initialValue || 0

  const name = document.createElement("input");
  name.className = "tally-name";
  name.value = "Tally " + ++numTallies;

  const output = document.createElement("input");
  output.type = "number";
  output.value = initialValue;
  output.className = "tally-box";

  const plus = document.createElement("button");
  plus.className = "tally-button plus";
  plus.innerText = "+";
  plus.onclick = () => ++output.value;

  const minus = document.createElement("button");
  minus.className = "tally-button minus";
  minus.innerText = "−"; // Minus sign, not ASCII
  minus.onclick = () => --output.value;

  const reset = document.createElement("button");
  reset.className = "tally-button reset";
  reset.innerText = "Reset";
  reset.onclick = () => output.value = 0;

  const root = document.createElement("div");
  root.className = "tally";
  root.appendChild(name);
  root.appendChild(output);
  root.appendChild(plus);
  root.appendChild(minus);
  root.appendChild(reset);

  fontLoad.then(value => {
    const lcdEffect = document.createElement("span");
    lcdEffect.className = "lcd-effect";
    root.appendChild(lcdEffect);
  })

  document.body.appendChild(root);
}

const button = document.createElement("button");
button.innerText = "Create Tally";
button.onclick = _ => addTally();
document.body.appendChild(button);
