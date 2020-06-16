export function indent(textBlock, linePrefix) {
  return textBlock.replace(/^/gm, linePrefix);
}

export function trimTrailing(textBlock) {
  return textBlock
    .trim()
    .replace(/\s+$/gm, "")
    + "\n";
}
