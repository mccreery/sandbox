#!/usr/bin/env python3

import praw, sys
reddit = praw.Reddit(user_agent="save-to-subreddit")
subreddit = reddit.subreddit(sys.argv[1])

total = 0
changed = True

while changed:
    changed = False

    for submission in subreddit.new(limit=None):
        changed = True
        submission.delete()
        total += 1

print("Deleted:", total)
