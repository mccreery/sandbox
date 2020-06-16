/* import fs from "fs";
import cheerio from "cheerio";
import prettier from "prettier";

const $ = cheerio.load(fs.readFileSync("index.html", "utf8"), { decodeEntities: false });
$("#content > p").after("<p>Another paragraph that is much longer than the first paragraph so I would only assume that this paragraph would end up wrapping onto the next line in some way right?");

fs.writeFileSync("index2.html", prettier.format($.html(), { parser: "html" }));
 */

import fs from "fs";
import htmlparser2 from "htmlparser2";

function formatAttrList(attrs) {
  let attrList = "";
  for (const attr in attrs) {
    attrList += ` ${attr}="${attrs[attr].replace(/(?=")/g, "\\")}"`;
  }
  return attrList;
}

function getLastLineIndex(text) {
  return Math.max(
    text.lastIndexOf("\r"),
    text.lastIndexOf("\n")
  ) + 1;
}

function getIndent(text) {
  const start = getLastLineIndex(text);

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

let currentIndent = 0;

const parser = new htmlparser2.Parser({
  onopentag(name, attribs) {
    process.stdout.write(`<${name}${formatAttrList(attribs)}>`);
  },
  ontext(text) {
    currentIndent = getIndent(text) ?? currentIndent;
    process.stdout.write(`${text} (current indent: ${currentIndent})`);
  },
  onclosetag(tagname) {
    process.stdout.write(`</${tagname}>`);
  },
  oncomment(data) {
    process.stdout.write(`<!--${data}-->`);
  }
});

parser.write(fs.readFileSync("main.html"));
