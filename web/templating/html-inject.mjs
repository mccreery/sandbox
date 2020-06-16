import fs from "fs";
import RewritingStream from "parse5-html-rewriting-stream";

function getIndent(text) {
  const start = Math.max(
    text.lastIndexOf("\r"),
    text.lastIndexOf("\n")
  ) + 1;

  // text doesn't contain a full line
  if (start === 0) {
    return null;
  }

  let end = start;
  while (end < text.length && text.charAt(end) == ' ') {
    ++end;
  }
  return end - start;
}

const rewriter = new RewritingStream();
let currentIndent = 0;

rewriter.on("text", textToken => {
  currentIndent = getIndent(textToken.text);
});

const inStream = fs.createReadStream("main.html");
const outStream = fs.createWriteStream("");
