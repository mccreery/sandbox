# Some Kind of Data Serialization Language

## Structures
There are three main structures: unnamed values, named values and lists.
- **Unnamed values** are standard values, conforming to one of the data types
  below. A "named" value with the empty string as its name is equivalent to an
  unnamed value.
- **Named values** are prefixed by a name and a colon, for example `option: 3`.
  If the name is empty, i.e. `: 3`, is an unnamed value.
- **Lists** are values separated by newlines or commas. More on this later.

## Data Types
Type    | Examples            | Notes
--------|---------------------|-------------------------------
Boolean | `true`, `false`     | Invalid identifiers
Integer | `3`, `-5`, `62455`  | No decimal points or exponents
Float   | `3.0`, `2e5`, `-4.` |
String  | `"Hello,\nworld"`<br>`'Hello,`<br>`world'` | Literal newlines can be escaped with `\`<br>Adjacent strings on a metaline are concatenated

## Block Contexts
Each named value has an optional *block context*, which starts on the same line.
Mainly intended for lists, the block context allows values to be split across
multiple lines. The block context ends before the first line whose indentation
is less than or equal to the line it opened on. Here are some examples:

```
blockA:
  1, 2, 3
  4, 5, 6
# blockA ends here
blockB: "list starting on first line"
  "and continuing on second line"
  nestedBlock: "a", "b", "c"
    insideNestedBlock: 3
  "outside nested block but inside blockB"

a: b: c: "inside all three contexts"
  "inside all three contexts"
"outside all three contexts"
```

### Commas and Newlines
Commas are roughly equivalent to a newline and an indent which is inside the
current block. This allows lists to be written on fewer lines or even a single
line:

```
list:
  1, 2, 3, 4, 5, 6
  7, 8, 9, 10, 11, 12

flatMatrix:
  1, 0, 0, 0
  0, 1, 0, 0
  0, 0, 1, 0
  0, 0, 0, 1

nestedMatrix:
  : 1, 0, 0, 0
  : 0, 1, 0, 0
  : 0, 0, 1, 0
  : 0, 0, 0, 1
```
