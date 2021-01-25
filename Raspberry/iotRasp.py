from picamera import PiCamera
from time import sleep
from datetime import datetime
import pyrebase
config ={
    
    "apiKey":       "AIzaSyC29H3xXuymo5_4eA8HWIN2ZaFkfT6FZbs",
    "authDomain":  "iot-rasp-34245.firebaseapp.com",
    "databaseURL":  "https://iot-rasp-34245-default-rtdb.firebaseio.com",
    "storageBucket": "iot-rasp-34245.appspot.com"    
}
def stream_handler(message):
    print(message["event"]) # put
    print(message["path"]) # /-K7yGTTEp7O549EzTYtI
    print(message["data"]) # {'title': 'Pyrebase', "body": "etc..."}



firebase = pyrebase.initialize_app(config)
db=firebase.database()
my_stream = db.child("ImgEnviadas").stream(stream_handler)

camera = PiCamera()
i=0;


while True:
    camera.start_preview()
    sleep(5)
    now=datetime.now()
    titulo =now.strftime("%d%m%Y%H%M");
    camera.capture('/home/pi/Desktop/imgCamara/%s.jpg'%titulo)
    camera.stop_preview
    storage = firebase.storage()
    
    storage.child("/Desktop/%s.jpg"%titulo).put("imgCamara/%s.jpg"%titulo)   
    print("%s"% i)
    i=i+1
    sleep(60)
    
print("FIN")    
    
