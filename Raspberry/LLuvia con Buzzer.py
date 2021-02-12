import serial
import time
try:
    port = serial.Serial("/dev/rfcomm0", baudrate=9600)
except:
    print("there is no bluetooth")
from time import sleep
from gpiozero import Buzzer, InputDevice


buzz    = Buzzer(13)
no_rain = InputDevice(18)
 
def buzz_now(iterations):
    for x in range(iterations):
        buzz.on()
        sleep(0.1)
        buzz.off()
        sleep(0.1)
print("hola")
while True:
    if  no_rain.is_active:
        print("It's raining - get the washing in!")
        try:
            port.write(b'Al')
        except:
            print("There is no bluetooth")
    else:
        print("no hay lluvia")
        # insert your other code or functions here
        # e.g. tweet, SMS, email, take a photo etc.
    sleep(1)