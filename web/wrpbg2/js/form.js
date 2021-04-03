const gamesElement = document.getElementById("games");
const gameElement = document.getElementById("game");
const categoryElement = document.getElementById("category");
const formElement = document.getElementById("form");
const urlElement = document.getElementById("url");

let gamesTimeout, categoriesTimeout;
let gameMap = {};
let categoryMap = {};

gameElement.addEventListener("input", () => {
  clearTimeout(gamesTimeout);

  gamesTimeout = setTimeout(() => {
    fetch(`https://www.speedrun.com/api/v1/games?name=${gameElement.value}`)
      .then(response => response.json())
      .then(json => {
        let html = "";

        for (const game of json.data) {
          gameMap[game.names.international] = game.id;
          html += `<option>${game.names.international}</option>`;
        }

        gamesElement.innerHTML = html;
      });
  }, 500);

  if (gameElement.value in gameMap) {
    categoryElement.disabled = false;

    fetch(`https://www.speedrun.com/api/v1/games/${gameMap[gameElement.value]}/categories`)
      .then(response => response.json())
      .then(json => {
        let html = "";

        for (const category of json.data) {
          categoryMap[category.name] = category.id;
          html += `<option>${category.name}</option>`;
        }

        categoryElement.innerHTML = html;
      });
  } else {
    categoryElement.disabled = true;
    categoryElement.value = "";
  }
});

const gameidElement = document.getElementById("gameid");
const categoryidElement = document.getElementById("categoryid");

gameElement.addEventListener("change", () => {
  gameidElement.value = gameMap[gameElement.value];
});

categoryElement.addEventListener("change", () => {
  categoryidElement.value = categoryMap[categoryElement.value];
});
