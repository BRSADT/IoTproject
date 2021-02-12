from gpiozero import MotionSensor
from signal import pause
from time import sleep

pir = MotionSensor(23)

while True:
    if pir.motion_detected:
        print("mov")
    else:
        print("no mov")
    sleep(1)
