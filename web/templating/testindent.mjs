import fs from "fs";
import getIndenter from "./indenter.mjs";

fs.createReadStream("main.html", { encoding: "utf8" })
  .pipe(getIndenter(4))
  .pipe(fs.createWriteStream("out.html"));
