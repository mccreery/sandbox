/**
 * Formats an amount of time in mm:ss or hh:mm:ss format.
 *
 * @param {number} seconds The number of seconds.
 * @return {string} The formatted time.
 */
export default function formatTime(seconds) {
  let hours = Math.floor(seconds / 3600);
  seconds -= hours * 3600;
  let minutes = Math.floor(seconds / 60);
  seconds -= minutes * 60;

  let output = minutes + (seconds < 10 ? ":0" : ":") + seconds.toFixed(1);
  if(hours > 0) {
    output = hours + (minutes < 10 ? ":0" : ":") + output;
  }
  return output;
}
