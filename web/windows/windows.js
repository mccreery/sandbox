var xOffset = 0, yOffset = 0;
var press = null;

var select = function(e) {
	xOffset = e.pageX - this.style.left.replace("px", "");
	yOffset = e.pageY - this.style.top.replace("px", "");
	press = this;
};
var deselect = function(e) {
	if(press == this) press = null;
};

var windows = document.getElementsByClassName("window");
for(var i = 0; i < windows.length; i++) {
	windows[i].onmousedown = select;
	windows[i].onmouseup = deselect;
}

document.onmousemove = function(e) {
	if(press != null) {
		press.style.left = (e.pageX - xOffset) + "px";
		press.style.top = (e.pageY - yOffset) + "px";
	}
};
