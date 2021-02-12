from picamera import PiCamera
    
print("aqui")    
camera = PiCamera()
print("no")
try:
        print("aqui")
        camera.start_preview()
        print("no")
		#DoCameraRelatedStuff
finally:
		camera.stop_preview()
		camera.close()