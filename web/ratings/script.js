function findRating(e, obj) {
	return Math.round((e.pageX - obj.offsetLeft) / obj.clientWidth * 10.0) / 2.0;
}

function encodeClass(n) {return "_" + n.toString().replace(".", "-");}
function findNumbers() {
	return this.className.split(" ").filter(function(name) {
		return name.match(/_[0-5](?:-5)?/)
	}).join(" ");
}

$("#rating").mousemove(function(e) {
	var n = encodeClass(findRating(e, this));

	if(!$(this).hasClass(n)) {
		$(this).removeClass(findNumbers).addClass(n);
	}
}).mouseout(function(e) {
	if($(this).hasClass("set")) {
		var n = encodeClass($(this).data("userset"));
		if(!$(this).hasClass(n)) {
			$(this).removeClass(findNumbers).addClass(n);
		}
	} else {
		$(this).attr("class", "rating _3-5");
	}
}).click(function(e) {
	$(this).addClass("set").data("userset", findRating(e, this));
});
