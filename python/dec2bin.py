def b(d):
    a = abs(d)
    b = ""
    while a:
        b = str(a % 2) + b
        a //= 2
    return ("-" if d < 0 else "") + (b if d else "0")

if __name__ == "__main__":
    for i in [255, 10, -10, -127]:
        print(str(i).rjust(5), "->", b(i))