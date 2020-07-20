export default function chunk(text, chunkSize, options = {}) {
  if (options.reverse) {
    return chunkBackward(text, chunkSize, options);
  } else {
    return chunkForward(text, chunkSize, options);
  }
}

function chunkForward(text, chunkSize, options) {
  const chunks = [];

  let i;
  for (i = 0;
      i < text.length && (!("max" in options) || chunks.length < options.max);
      i += chunkSize) {
    chunks.push(text.substring(i, i + chunkSize));
  }

  if (options.tail && i < text.length) {
    chunks.push(text.substring(i));
  }
  return chunks;
}

function chunkBackward(text, chunkSize, options) {
  const chunks = [];

  let i;
  for (i = text.length;
      i > 0 && (!("max" in options) || chunks.length < options.max);
      i -= chunkSize) {
    chunks.push(text.substring(i - chunkSize, i));
  }

  if (options.tail && i > 0) {
    chunks.push(text.substring(0, i));
  }
  return chunks;
}
