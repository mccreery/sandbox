import RPi.GPIO as gpio, time
gpio.setmode(gpio.BCM)

outputs = [18, 23, 24, 25, 12]
for pin in outputs: gpio.setup(pin, gpio.OUT)
gpio.setup(4, gpio.IN, pull_up_down=gpio.PUD_DOWN)

t = 0

states = [False] * 5

def output(func):
    for pin in range(len(outputs)):
        state = func(pin)
        if states[pin] != state: # Only change if actually changed
            gpio.output(outputs[pin], state)
        states[pin] = state

#-#-#-#-#-#-#-#-#-#-#-#-#

def alternate():
    output(lambda pin: pin % 2 == t % 2)

def loop():
    output(lambda pin: pin == t % len(outputs))

def flash():
    output(lambda pin: t % 2)

def bounce():
    output(lambda pin: pin == 4 - abs(4 - (t % 8)))

def dist():
    output(lambda pin: abs(2 - pin) < t % 4)

def fan():
    output(lambda pin: abs(2 - pin) == 2 - abs(2 - (t % 4)))

def change():
    delay = 20 # Ticks between changes
    modes[(t // delay) % (len(modes) - 1)]() # Don't include change within itself

modes = [alternate, loop, flash, bounce, dist, fan, change]
mode_names = ["Alternate", "Loop", "Flash", "Bounce", "Distance", "Fan", "Change"]
name_max = max(len(name) for name in mode_names)

#-#-#-#-#-#-#-#-#-#-#-#-#

speeds = [0.05, 0.1, 0.2, 0.5, 1.0]
speed_max = max(len(str(speed)) for speed in speeds)
speed = 0

def print_status():
    print("\rMode: " + mode_names[mode].ljust(name_max) + " Speed: " + str(speeds[speed]).ljust(speed_max), end="")

def set_mode(m):
    global mode
    mode = m % len(modes)
    print_status()
set_mode(-1)

def set_speed(s):
    global speed
    speed = s % len(speeds)
    print_status() 
set_speed(2)

next_tick = time.clock()
cooldown = 0
cooldown_fast = 0

try:
    while True:
        press = gpio.input(4)
        now = time.clock()

        while now >= next_tick:
            modes[mode]()
            t += 1
            next_tick += speeds[speed] 

        if press:
            if now > cooldown:
                set_mode(mode + 1) # Change mode
                cooldown = now + 0.5
                cooldown_fast = now + 0.3 
            elif now > cooldown_fast:
                set_speed(speed + 1)
                cooldown = now + 0.5
                cooldown_fast = now + 0.3
except KeyboardInterrupt:
    gpio.cleanup()
    print()
