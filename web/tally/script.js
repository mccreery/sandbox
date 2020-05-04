"use strict";

const fontLoad = document.fonts && document.fonts.load("italic bold 1em DSEG7");
const template = document.querySelector(".tally");
template.remove();

function enableButton(button, action) {
  button.onclick = action;
  button.disabled = false;
}

function createTally(name, value) {
  const tally = template.cloneNode(true);

  // Fill initial values
  const nameInput = tally.querySelector(".tally-name");
  const valueInput = tally.querySelector(".tally-box");
  nameInput.value = name || "Unnamed tally";
  valueInput.value = value || 0;

  // Only hide arrows when JS is enabled
  valueInput.classList.add("plain-number");

  // Add actions to buttons
  enableButton(tally.querySelector(".minus"), () => {
    --valueInput.value;
    saveToLocalStorage();
  });
  enableButton(tally.querySelector(".plus"), () => {
    ++valueInput.value;
    saveToLocalStorage();
  });
  enableButton(tally.querySelector(".reset"), () => {
    valueInput.value = 0;
    saveToLocalStorage
  });
  enableButton(tally.querySelector(".close-button"), () => {
    tally.parentNode.removeChild(tally);
    saveToLocalStorage();
  });

  // Add LCD effect where available
  fontLoad && fontLoad.then(() => {
    const lcdEffect = document.createElement("span");
    lcdEffect.className = "lcd-effect";
    tally.appendChild(lcdEffect);
  });

  // Handle writing to localStorage
  nameInput.oninput = saveToLocalStorage;
  valueInput.oninput = saveToLocalStorage;

  return tally;
}

const createButton = document.querySelector("#create");
enableButton(createButton, function() {
  this.parentNode.insertBefore(createTally(), this);
  saveToLocalStorage();
});

function saveToLocalStorage() {
  const list = [];

  for (const tally of document.querySelectorAll(".tally")) {
    list.push({
      name: tally.querySelector(".tally-name").value,
      value: tally.querySelector(".tally-box").value
    });
  }
  localStorage.setItem("tallies", JSON.stringify(list));
}

// Load tallies from localStorage
for (const entry of JSON.parse(localStorage.getItem("tallies"))
    || [{}]) { // Default 1 tally with default params
  createButton.parentNode.insertBefore(createTally(entry.name, entry.value), createButton);
}
