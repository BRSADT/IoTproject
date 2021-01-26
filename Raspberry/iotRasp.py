from picamera import PiCamera
from time import sleep
from datetime import datetime
import pyrebase
config ={
    
    "apiKey": "AIzaSyAOM_eU6X1MYFw2162jFbulgMo4jWUbzLg",
    "authDomain": "iotproject-9841c.firebaseapp.com",
    "databaseURL": "https://iotproject-9841c-default-rtdb.firebaseio.com",
    "projectId": "iotproject-9841c",
    "storageBucket": "iotproject-9841c.appspot.com",
    "messagingSenderId": "178302614682",
    "appId": "1:178302614682:web:05e900f1a4c57e4f317323",
    "measurementId": "G-3ZLQM1JE1C"
}
def stream_ProcesarImagenes(message):
    print(message["event"]) # put
    print(message["path"]) # /-K7yGTTEp7O549EzTYtI
    print(message["data"]) # {'title': 'Pyrebase', "body": "etc..."}
    datos= message["data"]
    print(datos)
    UID=datos['Usuario']['nameUID']
    print("usuario "+UID)
    path=datos['Usuario']['nombre']
    print("Path "+path)
    PathCompletoStorage="Procesamiento/"+UID+"/"+path
    print(PathCompletoStorage)
    PathRasp="/home/pi/Desktop/IoT/IoTproject/ImagenesProcesar/"+UID+".jpg"
    storage.child(PathCompletoStorage).download(PathRasp)


firebase = pyrebase.initialize_app(config)
db=firebase.database()
storage = firebase.storage()
my_stream = db.child("BaseDeDatos/SolicitudImagenes").stream(stream_ProcesarImagenes)

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
    
    #storage.child("/Desktop/%s.jpg"%titulo).put("imgCamara/%s.jpg"%titulo)   
    print("%s"% i)
    i=i+1
    sleep(60)
    
print("FIN")    
    
