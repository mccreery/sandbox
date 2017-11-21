function d(da) {return (new Date(da).getTime() / 1000).toFixed(0);}
var sub = prompt("Which sub?");
var start = prompt("Start");
var end = prompt("End", start);
start = d(start);
if(end) {end = d(end);}
else {end = start;}

end = parseInt(end) + 60*60*24;

document.location.replace("http://reddit.com/r/{}/search?syntax=cloudsearch&restrict_sr=on&sort=new&q=timestamp%3A{}..{}".format(sub, start, end));
