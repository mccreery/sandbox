#!/usr/bin/env python3

import requests, json

BASE = "https://api.github.com/"

repos = json.loads(requests.get(BASE + "users/mccreery/repos").text)
repos = (repo["full_name"] for repo in repos)

users = {}

for repo in repos:
	stats = json.loads(requests.get(BASE + "repos/" + repo + "/stats/contributors").text)

	for entry in stats:
		author = entry["author"]["login"]
		total = sum(week["a"] + week["d"] for week in entry["weeks"])

		if author in users:
			users[author] += total
		else:
			users[author] = total

		print("For repo {}, {} made {} changes".format(repo, author, total))

print(users)
