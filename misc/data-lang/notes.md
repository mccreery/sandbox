- Booleans are the literal nonquoted strings true and false
- Integers are [+-]?[1-9][0-9]*
  - e.g. 3 +3 -5 101 -1929482754784
- Floats are <int>(.<uint>)?([Ee]<int>)? but at least one of these
  - e.g. 3. 3.0 3e6 -5e-2 -15.e-2
- Dates are integer years, months, days (ISO) 1970-01-01T00:00Z
- Double quotes are standard strings including backslash escapes
- Single quotes ignore escapes, all characters are literal
- Both types of string can include literal newlines
- Backslashes can escape newlines both inside and outside of strings
- Adjacent strings (no newlines in between) are concatenated
- Identifiers are standard [a-zA-Z_][a-zA-Z0-9_]*
  - Equivalent to a string so quotes are optional on identifiers
  - The only invalid identifiers are true and false
- Blocks are opened by a colon
- A block ends before the first line whose indentation is <= that of the opening
  line containing the colon
- e.g.
    block:
      line inside block
      continued line \
      inside block
        varied indentation is fine
      but this:
        is a nested block
      still inside block
    not inside the block
- A comma is equivalent to a newline and the same indent as the current block
  - i.e. commas can replace newlines inside blocks
- A list is a block where each metaline is a value
- e.g.
    block:
      item 1
      item 2
      item 3
      multiple, items, on, one, line
      nested block still works: 1, 2, 3
- The order of a list is decided by the application, sets = lists = tuples
- A singular item turns into a list by a trailing comma but NOT an empty
  indented line
- Dictionaries are actually lists of named blocks
- The entire file is a block which will usually be a dictionary
- Most blocks will be on the same line
- Names can be of any type, not just strings (identifiers)

List of symbols: "'\\+-.:, \
List of keywords: true false

```
a: b: c: 3
  inside c context
outside all 3 contexts
```
