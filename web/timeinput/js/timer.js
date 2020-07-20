export default class Timer {
  endDate = null;
  pauseSeconds;
  interval;
  listeners = [];

  start(seconds) {
    seconds = seconds || pauseSeconds;
    this.endDate = new Date();
    this.endDate.setTime(this.endDate.getTime() + seconds * 1000);

    this.interval = setInterval(() => {
      for (const listener of this.listeners) {
        listener(this);
      }
    }, 50);
  }

  stop() {
    this.pauseSeconds = this.remaining;
    this.endDate = null;
    clearInterval(this.interval);
  }

  addListener(listener) {
    this.listeners.push(listener);
  }

  get remaining() {
    return Math.max(0, Math.ceil((this.endDate - new Date()) / 1000));
  }

  get running() {
    return this.endDate !== null;
  }
}
