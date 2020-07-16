"use strict";

/**
 * Applies CSS scale to a child element to fit snugly inside its parent.
 */
function fitElement(child) {
  const scale = Math.min(
    child.parentElement.offsetWidth / child.offsetWidth,
    child.parentElement.offsetWidth / child.offsetWidth
  );
  child.style.transform = `scale(${scale})`;
}

/**
 * Divides a quantity into units, each relative to the previous.
 * Uses truncated division, so a negative input produces negative outputs.
 *
 * @example
 * // Returns [3, 2, 1] (1 hour, 2 minutes and 3 seconds)
 * // 3723 = 3 + 2*60 + 1*60*60
 * divideUnits(3723, [60, 60])
 *
 * @param {number} units The quantity expressed in atomic units.
 * @param {number[]} relativeMultipliers An array of progressively larger units.
 *   The first element is a multiple of atomic units; each subsequent element
 *   is a multiple of the previous unit.
 * @returns {number[]} The quantity divided into multiples of each unit,
 *   starting with the atomic unit.
 */
function divideUnits(units, relativeMultipliers) {
  let multiples = [];
  let quotient;

  for (const multiplier of relativeMultipliers) {
    quotient = Math.trunc(units / multiplier);

    // Add remainder to units
    multiples.push(units - quotient * multiplier);
    // Express units as larger units
    units = quotient;
  }
  // Add the largest unit
  multiples.push(units);
  return multiples;
}

/**
 * Trims trailing or leading zeros up to a minimum length.
 *
 * @param {number[]} arr The original array.
 * @param {number} minLength The minimum length to stop trimming at.
 * @param {boolean} leading True to remove from the start instead of the end.
 * @returns {number[]} The original array with zeros trimmed.
 */
function trimZeros(arr, minLength = 0, leading = false) {
  while (arr.length > minLength) {
    let i = leading ? 0 : arr.length - 1;

    if (arr[i] === 0) {
      arr.splice(i, 1);
    } else {
      break;
    }
  }
  return arr;
}

/**
 * @returns A standard object corresponding to the URL search parameters.
 */
function getURLParams() {
  const params = new URLSearchParams(window.location.search);
  let paramsObj = {};

  for (const [key, value] of params.entries()) {
    paramsObj[key] = value;
  }
  return paramsObj;
}

/**
 * Finds the next local date/time matching the given string.
 *
 * @param {string} time A string representation of a number (hours),
 *  or 2 numbers separated by a colon (hh:mm).
 */
function getDate(time) {
  const now = new Date();
  let end = new Date(now);

  // Set time on current day
  const parts = time.split(":", 2);
  end.setHours(parseInt(parts[0]));
  if (parts.length == 2) {
    end.setMinutes(parseInt(parts[1]));
  } else {
    end.setMinutes(0);
  }
  end.setSeconds(0);

  // Time in the past, change to tomorrow
  if (end.getTime() <= now.getTime()) {
    end.setDate(end.getDate() + 1);
  }
  return end;
}

function formatTimer(seconds, minParts = 2, leadingZero = false) {
  let negative = seconds < 0;
  if (negative) {
    seconds = -seconds;
  }

  let parts = divideUnits(seconds, [60, 60]);
  parts.reverse();
  trimZeros(parts, minParts, true);

  for (let i = leadingZero ? 0 : 1; i < parts.length; i++) {
    parts[i] = parts[i].toString().padStart(2, "0");
  }

  let timer = parts.map(s => `<span class=\"part\">${s}</span>`).join("<span class=\"sep\"></span>");
  if (negative) {
    timer = "-" + timer;
  }
  return timer;
}

const params = getURLParams();
const timer = document.getElementById("timer");
// TODO temp
params.time = "16:00"
const end = getDate(params.time);

setInterval(() => {
  const seconds = Math.floor((end - new Date()) / 1000);
  if (seconds <= 0 && params.soon !== undefined) {
    timer.innerHTML = params.soon;
  } else {
    timer.innerHTML = formatTimer(seconds, params.minParts, params.leadingZero);
  }
  fitElement(timer);
}, 100);

const lightbox = document.getElementById("lightbox");
lightbox.style.visibility = "hidden";

document.onkeydown = event => {
  console.log(event);

  if (event.key === "?") {
    if (lightbox.style.visibility === "hidden") {
      lightbox.style.visibility = "visible";
    } else {
      lightbox.style.visibility = "hidden";
    }
  } else {
    lightbox.style.visibility = "hidden";
  }
};
