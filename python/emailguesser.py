import re

THRESHOLD = 0.5
TLDS = ("com", "uk", "net")
KNOWN_DOMAINS = ("gmail.com", "hotmail.co.uk", "outlook.com", "yahoo.com")

def similarity(a, b):
    c = list(a)
    d = list(b)
    if len(d) > len(c): # Make sure c is longer
        c, d = d, c

    total = 0
    for x in d:
        if x in c:
            total += 1
            c.remove(x)
    return total / len(d)

def suggest(test, values):
    similarities = {}
    for value in values:
        similarities[value] = similarity(test, value)
    key = max(similarities, key=similarities.get)

    if similarities[key] >= THRESHOLD:
        return key
    return None

def valid_domain(domain):
    parts = domain.split('.')
    if len(parts) < 2:
        return False, None

    if not parts[-1] in TLDS:
        return False, None
        #parts[-1] = suggest(parts[-1], TLDS)
        #return False, None if parts[-1] is None else ".".join(parts)
    if not domain in KNOWN_DOMAINS:
        domain = suggest(domain, KNOWN_DOMAINS)
        if domain is not None:
            return False, domain
    return True, None

def valid_name(name):
    for char in name:
        if not char.lower() in "abcdefghijklmnopqrstuvwxyz0123456789_-.":
            return False
    return True

email = ""

def valid_email():
    global email
    parts = email.split('@')

    if len(parts) != 2:
        return False
    if not valid_name(parts[0]):
        return False

    valid, suggestion = valid_domain(parts[1])
    if valid:
        return True
    elif suggestion is not None:
        new = "%s@%s" % (parts[0], suggestion)
        if input("*Did you mean: %s [y/n]? " % new).lower() in ("yes", "y"):
            email = new
            return True
        else:
            return True
    return False

while not valid_email():
    email = input("Enter your e-mail address: ")

print(email)