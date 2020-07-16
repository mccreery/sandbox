import { groupUnits, ungroupUnits } from "./units.mjs";

const splits = document.getElementById("splits");
const timestamps = document.getElementById("timestamps");
const offset = document.getElementById("offset");

function timestampToSeconds(timestamp) {
  return ungroupUnits(
    timestamp.split(":").reverse().map(x => parseInt(x)),
    [60, 60, 24]
  );
}

function secondsToTimestamp(seconds) {
  const groups = groupUnits(
    seconds,
    [60, 60, 24]
  );

  if (groups.length == 1) {
    groups.push(0);
  }

  return groups.reverse().map(x => x.toString().padStart(2, "0")).join(":").replace(/^0/, "");
}

function getColumn(table, colIndex) {
  const colArray = [];

  for (const line of table.split(/\r?\n/)) {
    colArray.push(line.split("\t")[colIndex]);
  }
  return colArray;
}

splits.oninput = offset.oninput = () => {
  const offsetValue = parseInt(offset.value);

  const names = getColumn(splits.value, 1);
  const times = getColumn(splits.value, 2).map(
    ts => timestampToSeconds(ts) + offsetValue
  );

  // Realign columns for the start of each segment
  times.unshift(offsetValue);
  names.push("Complete");

  // First timestamp must be 0
  if (times[0] > 0) {
    names.unshift("Intro")
    times.unshift(0);
  }

  timestamps.value = times.map((ts, i) => secondsToTimestamp(ts) + " - " + names[i]).join("\n");
};
