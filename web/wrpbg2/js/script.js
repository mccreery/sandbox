import ordinal from "./ordinals.js";
import formatTime from "./formattime.js";

// Find API IDs from category URL
const params = new URLSearchParams(window.location.search);

/**
 * @return {Promise<object>}
 */
function fetchJson(...args) {
  return fetch(...args).then(response => response.json());
}

/**
 * @return {Promise<object>} The personal bests data for the user.
 */
function personalBests(userId) {
  return fetchJson(`https://www.speedrun.com/api/v1/users/${userId}/personal-bests`);
}

/**
 * @param {string} gameId The game ID from speedrun.com.
 */
function worldRecord(gameId, categoryId) {
  return fetchJson(`https://www.speedrun.com/api/v1/leaderboards/${gameId}/category/${categoryId}`);
}

/**
 * @param {URL} url
 */
async function parseUrl(url) {
  let abbreviation = url.pathname.slice(1).split("/", 1)[0];
  let gameId, categoryId;

  if (url.hash) {
    const gameJson = await fetchJson(`https://www.speedrun.com/api/v1/games/${abbreviation}`);
    gameId = gameJson.data.id;

    const categoryJson = await fetchJson(`https://www.speedrun.com/api/v1/games/${abbreviation}/categories`);
    categoryId = categoryJson.data.find(category => category.weblink == url).id;
  } else {

  }

  return { gameId, categoryId };
}

parseUrl(new URL(params.get("url"))).then(console.log);

/*function update() {
  $.ajax({dataType: "json", url: "data", success: function(json) {
    var place = json["pb"]["place"];
    var placeDisplay = place + "<sup>" + niceExt(place) + "</sup>";

    $("#pb").html(placeDisplay + " <span class=\"time\">" + generateTime(json["pb"]["times"]["primary_t"]) + "</span>");

    flag = "<span class=\"flag " + json["wr"]["player"]["location"]["country"]["code"].toLowerCase() + "\"></span>";

    $("#wr").html("<span class=\"time\">" + generateTime(json["wr"]["times"]["primary_t"]) + "</span> by " + flag + json["wr"]["player"]["names"]["international"]);
  }});
}*/

update();
setInterval(update, 300_000);
