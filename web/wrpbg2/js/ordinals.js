/**
 * Adds ordinal suffixes to natural numbers.
 *
 * @param {number} n A positive integer.
 * @return {string} The ordinal representation of that integer.
 *
 * @example ordinal(3) == "3rd"
 * @example ordinal(101) == "101st"
 */
export default function ordinal(n) {
  const rem = n % 100;

  if (rem >= 11 && rem <= 13) {
    return n + "th";
  } else switch (n % 10) {
    case 1: return n + "st";
    case 2: return n + "nd";
    case 3: return n + "rd";
    default: return n + "th";
  }
}
