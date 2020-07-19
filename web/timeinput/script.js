const input = document.getElementById("input");
const dots = document.getElementById("dots");

function inputRemove(input, regexp) {
  let selectionStart = input.selectionStart;
  let selectionEnd = input.selectionEnd;

  for (const match of input.value.matchAll(regexp)) {
    if (match.index + match[0].length <= input.selectionStart) {
      selectionStart -= match[0].length;
    } else if (match.index <= input.selectionStart) {
      selectionStart -= input.selectionStart - match.index;
    }

    if (match.index + match[0].length <= input.selectionEnd) {
      selectionEnd -= match[0].length;
    } else if (match.index <= input.selectionEnd) {
      selectionEnd -= input.selectionEnd - match.index;
    }
  }

  input.value = input.value.replace(regexp, "");
  input.selectionStart = selectionStart;
  input.selectionEnd = selectionEnd;
}

function chunkRight(text, chunkSize, maxChunks) {
  const chunks = [];

  let rem = text.length % chunkSize;
  if (maxChunks !== undefined) {
    rem = Math.max(rem, text.length - chunkSize * (maxChunks - 1));
  }

  if (rem > 0) {
    chunks.push(text.slice(0, rem));
  }

  for (let i = rem; i < text.length; i += chunkSize) {
    chunks.push(text.slice(i, i + chunkSize));
  }
  return chunks;
}

function updateDots() {
  // dot then two "all off" characters
  dots.value = ".!!".repeat(Math.min(2, (input.value.length - 1) / 2));
}

input.addEventListener("input", () => {
  inputRemove(input, /[^\d]+/g);
  updateDots();
});

input.addEventListener("focus", () => {
  stopTimer();
  input.selectionStart = 0;
  input.selectionEnd = input.value.length;
  inputRemove(input, /[^\d]+/g);
  updateDots();
  dots.classList.add("show");
});

input.addEventListener("blur", () => {
  dots.classList.remove("show");
  input.value = chunkRight(input.value, 2, 3).join(":");
});

function groupUnits(atoms, groupSizes) {
  const groups = [];

  for (const groupSize of groupSizes) {
    const div = Math.trunc(atoms / groupSize);
    const rem = atoms - div * groupSize;

    groups.push(rem);
    atoms = div;

    if (atoms == 0) {
      return groups;
    }
  }
  groups.push(atoms);
  return groups;
}

function ungroupUnits(groups, groupSizes) {
  let atoms = groups[groups.length - 1];

  for (let i = groups.length - 2; i >= 0; i--) {
    atoms *= groupSizes[i];
    atoms += groups[i];
  }
  return atoms;
}

let endDate = null;
function startTimer() {
  const groups = input.value.split(":").reverse().map(x => parseInt(x));
  const seconds = ungroupUnits(groups, [60, 60]);

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

    input.value = groupUnits(seconds, [60, 60])
      .reverse()
      .map(group => group.toString().padStart(2, "0"))
      .join(":");
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
