from gpiozero import DistanceSensor
from time import sleep

sensor = DistanceSensor(23, 24)

while True:
    
    if sensor.distance>.6:
        print('Deteccion', sensor.distance, 'm')
    sleep(1)