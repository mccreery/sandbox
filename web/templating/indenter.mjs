import fs, { readFile } from "fs";
import RewritingStream from "parse5-html-rewriting-stream";

function readFileIndented(filename, indentSize) {
  return new Promise(resolve => {
    let text = "";
    const stream = fs.createReadStream(filename, { encoding: "utf8" })
      .on("data", chunk => text += chunk)
      .on("end", resolve(text))
      .pipe(getIndenter(indentSize + 2));
  });
}

export default function getIndenter(indentSize) {
  // Disabled within <pre></pre> tags
  let allowIndent = true;
  let pendingIndent = true;
  const indent = " ".repeat(indentSize);
  const indenter = new RewritingStream();

  const callback = (_, rawHtml) => {
    if (pendingIndent) {
      indenter.emitRaw(indent);
      pendingIndent = false;
    }

    if (allowIndent) {
      // Indent all but first and empty lines
      // or: insert indent after each group of newlines
      // except at the end of the string

      rawHtml = rawHtml.replace(/(?<=[\r\n])(?=[^\r\n])/g, indent);
      //rawHtml = rawHtml.replace(/([\r\n]+)([^\n])/g, `$1${indent}$2`);
      //rawHtml = rawHtml.replace(/(?<=\n)(?!\n|$)/g, indent);

      // Next token is at the very start of a line so defer indent
      // If at the end of the file no indent will happen
      pendingIndent = /[\r\n]$/.test(rawHtml);
    }
    indenter.emitRaw(rawHtml);
  }

  indenter
    .on("startTag", (startTag, rawHtml) => {
      callback(startTag, rawHtml);
      if (startTag.tagName === "pre") {
        allowIndent = false;
      }
    })
    .on("endTag", (endTag, rawHtml) => {
      if (endTag.tagName === "pre") {
        allowIndent = true;
      }
      callback(endTag, rawHtml);
    })
    .on("comment", callback)
    .on("text", callback)
    .on("doctype", callback);

  return indenter;
}
