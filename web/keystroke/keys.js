const buttons = [
  ["W", 87, 38, 0, 64, 64],
  ["A", 65, 0, 78, 64, 64],
  ["S", 83, 74, 78, 64, 64],
  ["D", 68, 148, 78, 64, 64],
  ["E", 69, 112, 0, 64, 64],
  ["Ctrl", 162, 0, 156, 101, 64],
  ["␣", 32, 111, 156, 101, 64],
  ["", 1, 222, 0, 64, 220],
  ["", 2, 296, 0, 64, 220],
  ["", 4, 283, 48, 16, 32]
];

buttons.forEach(function(x) {
  var button = document.createElement("div");
  button.className = "button";
  button.style.left = x[2];
  button.style.top = x[3];
  button.style.width = x[4];
  button.style.height = x[5];
  button.innerHTML = x[0];
  x[0] = button;
  document.body.appendChild(button);
});
buttons[9][0].id = "scroll";

function stateChange(code, state) {
  //console.log(code, state);
  for(var i = 0; i < buttons.length; i++) {
    if(code == buttons[i][1]) {
      buttons[i][0].className = state ? "button pressed" : "button";
      return false;
    }
  }
  return true;
}

var scrollTimeout;
KeystrokeClient.onKeyDown = function(code) {
  if(code === 10 || code === 11) {
    buttons[9][0].className = "button scrolling";
    clearTimeout(scrollTimeout);
    scrollTimeout = setTimeout(function() {
      buttons[9][0].className = "button";
    }, 50);
    return false;
  }
  return stateChange(code, true);
};
KeystrokeClient.onKeyUp = function(code) {return stateChange(code, false);};

KeystrokeClient.start(host_ip, host_port);
