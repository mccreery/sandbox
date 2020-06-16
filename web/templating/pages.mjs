#!/usr/bin/env node
import fs from "fs";
import path from "path";
import commonmark from "commonmark";
import post from "./post.mjs";

const parser = new commonmark.Parser({smart: true})
const writer = new commonmark.HtmlRenderer()

async function build(srcRoot, dstRoot, subDir) {
  subDir = subDir || "";

  const srcDir = path.join(srcRoot, subDir);
  const dstDir = path.join(dstRoot, subDir);
  await fs.promises.mkdir(dstDir, { recursive: true });

  const dirStream = await fs.promises.opendir(srcDir);
  for await (const entry of dirStream) {
    const subPath = path.join(subDir, entry.name);

    if (entry.isDirectory()) {
      await build(srcRoot, dstRoot, subPath);
    } else if (subPath.endsWith(".md")) {
      const srcPath = path.join(srcRoot, subPath);
      const dstPath = path.join(dstRoot, subPath.replace(/\.md$/, ".html"));

      const inFile = await fs.promises.readFile(srcPath, "utf8");
      const ast = parser.parse(inFile);
      const html = writer.render(ast);
      await fs.promises.writeFile(dstPath, html);
    }
  }
}

build("md", "html");
