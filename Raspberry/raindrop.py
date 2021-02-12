# raindrop sensor DO connected to GPIO18
# HIGH = no rain, LOW = rain detected
# Buzzer on GPIO13
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
        #No esta lloviendo
        print("No rain")
        buzz_now(5)
    else:
        #esta lloviendo
        print("rain")
        
    sleep(1)