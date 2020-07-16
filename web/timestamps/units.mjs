/**
 * Converts a number of atomic units into a positional system.
 *
 * @example
 * groupUnits(3723, [60, 60]) === [3, 2, 1] // 1 hour, 2 minutes, 3 seconds
 * groupUnits(10, [2, 2, 2]) === [0, 1, 0, 1] // binary 1010
 *
 * @param {number} atoms
 * @param {number[]} groupSizes An n sized array
 * @returns {number[]} An n+1 sized array starting with the remainder of atoms
 */
export function groupUnits(atoms, groupSizes) {
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

/**
 * Converts a number from a positional system to atomic units.
 *
 * @param {number[]} groups
 * @param {number[]} groupSizes
 * @returns {number} The number of atomic units.
 */
export function ungroupUnits(groups, groupSizes) {
  let atoms = groups[groups.length - 1];

  for (let i = groups.length - 2; i >= 0; i--) {
    atoms *= groupSizes[i];
    atoms += groups[i];
  }
  return atoms;
}
