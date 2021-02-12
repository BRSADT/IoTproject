from picamera import PiCamera
from time import sleep
camera = PiCamera()
camera.stop_preview()
camera.start_preview()
sleep(5)
camera.capture('fotoCarawhileStreaming.jpg')
print("foto Tomada")