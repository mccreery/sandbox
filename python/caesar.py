ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

print("*** Caesar Cipher ***")

shift = int(input("Please enter a number from -26 to 26: "))
if input("Press 'd' to decipher, anything else to encipher: ").lower() == "d":
    msg = input("Please enter your message: ")

    decrypted = []
    for char in msg:
        index = ALPHABET.index(char)
        index = (index - shift) % len(ALPHABET)
        decrypted.append(ALPHABET[index])

    print("Here is the deciphered message:")
    print("".join(decrypted))
else:
    msg = input("Please enter your message: ")

    msg = msg.upper().replace(" ", "")

    encrypted = []
    for char in msg:
        index = ALPHABET.index(char)
        index = (index + shift) % len(ALPHABET)
        encrypted.append(ALPHABET[index])

    print("Here is the enciphered message:")
    print("".join(encrypted))