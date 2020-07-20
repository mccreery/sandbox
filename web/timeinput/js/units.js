export function group(atoms, groupSizes) {
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

export function ungroup(groups, groupSizes) {
  let atoms = groups[groups.length - 1];

  for (let i = groups.length - 2; i >= 0; i--) {
    atoms *= groupSizes[i];
    atoms += groups[i];
  }
  return atoms;
}
