import tkinter as tk
from tkinter import ttk
from tkinter.filedialog import asksaveasfilename
from tkinter import N, E, S, W

ICON = "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAILSURBVDhPnZMxb9QwFMef7dwlzekuRoVWOgbajYmqdL6lS4fyDVhRGRgq9YNU6tCl3AfgAyCkLrd0RogVhnaquKqlTo7eJRc75u+7BEUZQMLST7bf+/+fHUePUWPsx6fbXREeSO7vSiY2I8SkNZeyyEZdMz17Fb37slQux58CL66OVzqePInEozeSekxSQJI8gAKkKaIUc2Ijcz/saHW4tXE0c75FgWdfj1fC1uPziK0PJGSSQtAGHLgCBQrMMU+BosiOL8L8dm9j62jGXQH9IE9svD4gtUqkYFE90G3gYi63SgxaDx7nZb2Pp9uh6H+W1Me1q9Or6wvgbmBwA43ZfcbyFpKubWiud7iZhAeozkiFqB6U+CXtkmpf5Z22xwS8vFD+LsUIxhDGLeD9A6dx2oA4vNwqsUkKCYXnWCBq6yb1nEcMXk4xPlL9J/BypswlKY1AUWJq6yamttZk4eVcZSNSKQJzkC8Sf8dpnDalAl4uJtMzUoklNV0El2QlTuio9lXeaRNr4BXZ908/oid7T/20vROkgoKUAwsKoEFekoEZmIB7amU3w7XR2/d4TiLvQR2yeHxB6g6V8ToqAZMGLuZyd2Sh1fA4L/4LHnN8rvudwYd2mq8FM/0SJ7MgnddO/QVicGv9/GYozM/Xz7/Vmqk+9ntoZ4N2LtDOtmxnhnbmaGeBdk7q7Uz0G3h3ViQCwpkqAAAAAElFTkSuQmCC"

FILE_TYPES = (
  ("PNG", "*.png"),
  ("DXT1", "*.dds"),
  ("DXT3", "*.dds"),
  ("DXT5", "*.dds")
)

def save():
  filename = asksaveasfilename(filetypes=FILE_TYPES, defaultextension=".png")

master = tk.Tk()
master.resizable(False, False)
master.iconphoto(False, tk.PhotoImage(data=ICON))
master.title("Normal Map Generator")

POTS = list(1<<i for i in range(5, 15))

ttk.Label(master, text="Size").grid(row=0, column=0, sticky=E)
size = tk.IntVar(master, POTS[0])
size_widget = ttk.Spinbox(master, textvariable=size, values=POTS).grid(row=0, column=1, sticky=E+W, padx=5)

ttk.Label(master, text="Edge").grid(row=1, column=0, sticky=E)
edge = tk.StringVar(master)
edge_widget = ttk.OptionMenu(master, edge, "Clamp", "Clamp", "Flat").grid(row=1, column=1, sticky=E+W, padx=5)

ttk.Label(master, text="Options").grid(row=2, column=0, sticky=E)

invert_y = tk.BooleanVar(master, True)
invert_y_widget = ttk.Checkbutton(master, text="Invert Y", variable=invert_y).grid(row=2, column=1, sticky=E+W, padx=5)

antialias = tk.BooleanVar(master, True)
antialias = ttk.Checkbutton(master, text="Antialiasing", variable=antialias).grid(row=3, column=1, sticky=E+W, padx=5)

ttk.Button(master, text="Save As...", command=save).grid(row=4, columnspan=2, sticky=E, padx=5)

tk.Grid.columnconfigure(master, 0, pad=10, minsize=100)
tk.Grid.columnconfigure(master, 1, pad=10, minsize=200)

for i in range(5):
  tk.Grid.rowconfigure(master, i, pad=5)

master.mainloop()
