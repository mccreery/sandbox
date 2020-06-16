import { indent, trimTrailing } from "./formatting.mjs";

export default function post(title, body) {
  const page = `
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>${title}</title>
    <link rel="stylesheet" href="style.css">
  </head>
  <body>
${indent(body, "    ")}
  </body>
</html>
  `;
  return trimTrailing(page);
}
