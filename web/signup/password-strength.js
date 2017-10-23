var reason, pws = ["password", "letmein", "password1", "password12", "password123"];

var mark, note;
function init() {
	mark = document.getElementById("secure");
	note = document.getElementById("reason");
}

function test(obj) {
	update(valid(obj.value));
}

function valid(pw) {
	if(pw.length == 0) {
		reason = "Please enter a password";
		return false;
	} else if(pw.length < 8) {
		reason = "Too short";
		return false;
	} else if(pws.indexOf(pw) >= 0) {
		reason = "Common password";
		return false;
	}
	return true;
}

function update(x) {
	mark.innerHTML = x ? "\u2713" : "\u2717";
	note.innerHTML = x ? "" : reason;
}
