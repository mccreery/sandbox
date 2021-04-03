// Extend click area of card without blocking text selection
for (const card of document.getElementsByClassName("card")) {
  const link = card.getElementsByClassName("card-link")[0];

  if (!link) {
    continue;
  }

  // The card cannot be dragged, but moving the mouse during the click breaks it
  let clicking;
  card.addEventListener("mousedown", () => clicking = true);
  card.addEventListener("mousemove", () => clicking = false);

  card.addEventListener("click", event => {
    if (clicking && event.target !== link) {
      link.click();
    }
  });
}
