let endDate = null;
let pauseSeconds;

export function start(seconds) {
  seconds = seconds || pauseSeconds;
  endDate = new Date();
  endDate.setTime(endDate.getTime() + seconds * 1000);
}

export function stop() {
  pauseSeconds = remaining();
  endDate = null;
}

export function running() {
  return endDate !== null;
}

export function remaining() {
  return Math.max(0, Math.ceil((endDate - new Date()) / 1000));
}

setInterval(() => {
  if (running()) {
    document.dispatchEvent(new CustomEvent("timer", { detail: remaining() }));
  }
}, 50);
