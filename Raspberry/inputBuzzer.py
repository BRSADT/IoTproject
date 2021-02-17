from gpiozero import Button,Buzzer
from time import sleep
button= Button(2)
buzz    = Buzzer(26)
while True:
    if button.is_pressed:
        print("Button is pressed")
        buzz.on()
        sleep(0.1)
        buzz.off()
        sleep(0.1)
    else:
        buzz.on()
        sleep(0.4)
        buzz.off()
        sleep(0.1)
        print ("Button is not pressed")
            