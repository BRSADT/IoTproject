import time
import serial
try:
    port = serial.Serial("/dev/rfcomm0", baudrate=9600)
except:
    print("there is no bluetooth")
from picamera import PiCamera
from time import sleep
from datetime import datetime
import pyrebase
import boto3
import os 
from botocore.exceptions import ClientError
from gpiozero import DistanceSensor,InputDevice
#Streaming
import Streaming
import os,signal
from os import fork,kill
from signal import signal,SIGINT
from sys import exit
global flag
flag=False
global s
s=Streaming
global CameraNotBusy
CameraNotBusy=True
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
#Controlan las funciones de streaming para que no se ejecuten al inicio
global inicioS4
i=0
inicioS1=0
inicioS2=0
inicioS3=0
inicioS4=0
inicioS5=0
#STREAMING
#Maneja la señal para detener el streaming
def handle_signal(sig, frame):
    global s
    global CameraNotBusy
    print('señall'+str(sig))
    if sig == SIGINT:
        print("dying")
        CameraNotBusy=True
        s.StopStream()
        
def CrearFork():#Paralelo
    global CameraNotBusy
    pid=os.fork()
    
    if pid ==0 :
            
            global s
            
            print("proceso hijo")
            signal(SIGINT,handle_signal)
            s.StartStream()
    else:
        print("entro padre")
        global flag
        firebase = pyrebase.initialize_app(config)
        db=firebase.database()
        stream_Camara = db.child("BaseDeDatos/Camara").stream(stream2_Camara)
        while flag==False:
            print("wait")
            sleep(2)
        print("se activo, muere hijo")
        CameraNotBusy=True;
        kill(pid,SIGINT)        

#AWS
def RekognitionAttributes(file_name, bucket):  
    client = boto3.client('rekognition')
    response=client.detect_faces(
        Image={
           
            'S3Object': {
                    'Bucket' : bucket,
                    'Name' : file_name
                                
                }
            },
        Attributes=[
              'ALL'
            ]
        )
    return response


 
        
def AgregarRostro(path,Name,ImageId):
    client = boto3.client('rekognition')
    response=client.index_faces(
        CollectionId='UsuariosPermitidos',
        Image={
       
            'S3Object': {
                    'Bucket' : "raspsam", # raspsam/FotosPersonas
                    'Name' : path  #NombreX
                            
            }
        },
    ExternalImageId=ImageId,
    DetectionAttributes=[
          'ALL'
        ],
    MaxFaces=1,
    QualityFilter='AUTO'
    )
    return response

def upload_file(file_name, bucket, object_name=None):
   
    print("S3")
    # If S3 object_name was not specified, use file_name
    if object_name is None:
        object_name = file_name

    # Upload the file
    
    s3_client = boto3.client('s3')
    try:
        response = s3_client.upload_file(file_name, bucket, object_name)
       
    except ClientError as e:
        logging.error(e)
        print(e)
        return False
    return True

#Streams de Firebase, funciones que se ejecutan cuando detectan un cambio en el path

    #streaming
def stream_Camara(datos):
      global CameraNotBusy
      global inicioS4
      global flag
      if inicioS4!=0:
        CameraNotBusy=False;
        print("aleeerta para streaming")  
        global modoAlerta
        CMR=datos["data"]
        if CMR=="Streaming":
            print("Streaming")
            flag=False
            CrearFork()
        else:
            CameraNotBusy=True;
      else:
          inicioS4=1
          
          
def stream2_Camara(datos):
      global inicioS4
      global flag
      if inicioS4!=0:     
        global modoAlerta
        CMR=datos["data"]
        if CMR=="Streaming":       
            print("Streaming")       
        else:
            flag=True
            print("No Streaming")            
      else:
          inicioS4=1
    #Normal          

def stream_ProcesarImagenes(datos):
    global inicioS1
    print("evento")
    if inicioS1!=0:
        print(datos["data"]) # {'title': 'Pyrebase', "body": "etc..."}
        #datos=message["data"]['Usuario'];
        #print(datos)
        UID=datos["data"]['nameUID']
        print("usuario "+UID)
        path=datos["data"]['nombre']
        print("Path "+path)
        PathCompletoStorage="Procesamiento/"+UID+"/"+path
        print("Storage "+PathCompletoStorage)
        PathRasp="/home/pi/Desktop/IoT/IoTproject/ImagenesProcesar/"+UID+"/"+path+".jpg"
        if not os.path.exists("/home/pi/Desktop/IoT/IoTproject/ImagenesProcesar/"+UID):
            os.makedirs("/home/pi/Desktop/IoT/IoTproject/ImagenesProcesar/"+UID)
        #pathRaspBerry=PathRasp+"/"+path+".jpg"        
        print("path rasp "+PathRasp)
        #storage.child("Procesamiento").download("/home/pi/Desktop/IoT/IoTproject/ImagenesProcesar/Procesamiento.jpg") #aqui bien
        #storage.child("Procesamiento/2PRU3spGgcg8S4AMJl96NQCWpAH3").download("/home/pi/Desktop/IoT/IoTproject/ImagenesProcesar/Adam.jpg") #aqui bien
       # storage.child(PathCompletoStorage).download("/home/pi/Desktop/IoT/IoTproject/ImagenesProcesar/Adam.jpg") #aqui bien
        
        storage.child(PathCompletoStorage).download(PathRasp) #aqui bien
        PathAWS="FotosAnalisis/"+UID+".jpg"
        upload_file(PathRasp,'raspsam',PathAWS)
        response=RekognitionAttributes(PathAWS,'raspsam')
        print("aquii")
        print(response)
        print(response['FaceDetails'])
        #print(response['FaceDetails']['BoundingBox']['Smile']['Value']+" "+response['FaceDetails']['BoundingBox']['Smile']['Confidence'])
        #emociones=response['FaceDetails']['BoundingBox']['Emotions']
        #for emo in emociones:
         #   print (emo['Type']['Confidence'])
        for faceDetail in response['FaceDetails']:
            print("Su edad esta entre "+str(faceDetail['AgeRange']['Low'])+" y "+str(faceDetail['AgeRange']['High']))
            print("Parece ser "+faceDetail['Gender']['Value']+" en "+str(faceDetail['Gender']['Confidence'])+" %")
            print("Parece tener barba "+str(faceDetail['Beard']['Value'])+" en "+str(faceDetail['Beard']['Confidence'])+" %")
            print("Sus emociones son")
            for Emotions in faceDetail['Emotions']:
                    print("Tipo de emocion "+Emotions['Type']+" "+str(Emotions['Confidence'])+" %")
            print("ojos abiertos "+str(faceDetail['EyesOpen']['Value'])+" en "+str(faceDetail['EyesOpen']['Confidence'])+" %")
            print("Anteojos "+str(faceDetail['Eyeglasses']['Value'])+" en "+str(faceDetail['Eyeglasses']['Confidence'])+" %")
            print("Bigote "+str(faceDetail['Mustache']['Value'])+" en "+str(faceDetail['Mustache']['Confidence'])+" %")
            print("Sonrisa "+str(faceDetail['Smile']['Value'])+" en "+str(faceDetail['Smile']['Confidence'])+" %")
            print("Sunglasses "+str(faceDetail['Sunglasses']['Value'])+" en "+str(faceDetail['Sunglasses']['Confidence'])+" %")
                  
        db.child("RespuestaAnalisis").child(UID).set(response['FaceDetails'])    
    else:
        inicioS1=1
def stream_CambioSensores(datos):
      global inicioS2
      if inicioS2!=0:
        print(datos["data"]) # {'title': 'Pyrebase', "body": "etc..."}
        global modoAlerta
        modoAlerta=datos["data"]["modoAlerta"]
        segundos=datos["data"]['segundos']
        global modoLluvia
        modoLluvia=datos["data"]['alertaLluvia']
        global modoLlama
        modoLlama=datos["data"]['alertaLlama']
        global TimeForPicture
        TimeForPicture=segundos
      else:
          inicioS2=1
          
def stream_TomarFoto(datos):
      global inicioS5
      global CameraNotBusy
      if inicioS5!=0:
        CameraNotBusy=False
        print(datos["data"]) # {'title': 'Pyrebase', "body": "etc..."}
        if datos["data"]=="Si":
            TomarFoto()
            CameraNotBusy=True
            data = {"No"}
            db.child("BaseDeDatos").child("TomarFoto").set("No")
  
      else:
          inicioS5=1  


def stream_CambioSensoresFoto(datos):
      global inicioS3
      if inicioS3!=0:
        print(datos["data"]) # {'title': 'Pyrebase', "body": "etc..."}
        global modoAlerta
        modoAlerta=datos["data"]['modoAlerta']
        segundos=datos["data"]['segundos']
        global TimeForPicture
        TimeForPicture=segundos
        idFoto=datos["data"]['nombreFoto']
        path=datos["data"]['ruta']
        print("Path "+path)
        PathCompletoStorage="PersonasReconocidas/"+path+"/"+idFoto
        print("Storage "+PathCompletoStorage)
        PathRasp="/home/pi/Desktop/IoT/IoTproject/PersonasReconocidas/"+path+"/"+idFoto+".jpg"
        if not os.path.exists("/home/pi/Desktop/IoT/IoTproject/PersonasReconocidas/"+path):
            os.makedirs("/home/pi/Desktop/IoT/IoTproject/PersonasReconocidas/"+path)
        storage.child(PathCompletoStorage).download(PathRasp) #aqui bien
        print("path rasp "+PathRasp)
        PathAWS="FotosPersonas/"+idFoto+".jpg"
        upload_file(PathRasp,'raspsam',PathAWS)
        pathRek="FotosPersonas/"+idFoto+".jpg"
        idFotoe=datos["data"]['idFoto']
        res=AgregarRostro(pathRek,idFotoe,idFoto)
        print(res)
      else:
         inicioS3=1
def TomarFoto():
            camera = PiCamera();
            print("TomaraFoto")
            now=datetime.now()
            titulo =now.strftime("%d%m%Y%H%M");
            camera.start_preview()
            sleep(5)
            camera.capture('/home/pi/Desktop/IoT/IoTproject/FotosTomadas/Foto.jpg')
            print("foto tomada")
            camera.stop_preview()
            camera.close()
            now=datetime.now()
            tituloFoto =now.strftime("%d%m%Y%H%M");
            print("fecha",tituloFoto)
            hora=now.strftime("%H:%M");
            fecha=now.strftime("%d/%m/%Y");
            print("hora",hora)
            print("fecha",fecha)
            storage.child("FotosTomadas/"+tituloFoto+".jpg").put("/home/pi/Desktop/IoT/IoTproject/FotosTomadas/Foto.jpg")
            ruta="FotosTomadas/"+tituloFoto+".jpg"
            data = {"Fecha": fecha ,"Hora": hora , "Ruta":ruta, "Tipo": "Registro" }
            db.child("BaseDeDatos").child("HistorialSensores").child(tituloFoto).set(data)
            
         
modoAlerta="false";
modoLluvia="false";
modoLlama="false";

firebase = pyrebase.initialize_app(config)
db=firebase.database()
storage = firebase.storage()
ellapsedPicture=0;
startTimePicture=time.time()
TimeForPicture=250
sensor = DistanceSensor(23, 24)
no_rain = InputDevice(18)
#Streams Firebase
my_stream = db.child("BaseDeDatos/SolicitudImagenes").stream(stream_ProcesarImagenes)
second_stream = db.child("BaseDeDatos/CambioSensores").stream(stream_CambioSensores)
third_stream = db.child("BaseDeDatos/CambioSensoresFoto").stream(stream_CambioSensoresFoto)
stream_Camara = db.child("BaseDeDatos/Camara").stream(stream_Camara)
stream_TomarFoto = db.child("BaseDeDatos/TomarFoto").stream(stream_TomarFoto)



while True:
    print("estado streaming"+str(CameraNotBusy))
    if CameraNotBusy==True:
        ellapsedPicture=(time.time()-startTimePicture)
        print (ellapsedPicture)
        if(ellapsedPicture>float(TimeForPicture)):#tomara datos de los sensores
            TomarFoto()          
            print("%s"% i)
            i=i+1
            startTimePicture=time.time()
    #Lecturas 
    if modoAlerta=="true":
        print(sensor.distance)
        if sensor.distance<.03:
            print("Movimiento")
            data = {"Alerta": "Movimiento"}
            db.child("BaseDeDatos").child("Sensores").set(data)
            sleep(20)
            data = {"Alerta": "None"}
            db.child("BaseDeDatos").child("Sensores").set(data)
            
    if modoLluvia=="true":
        print("detectara lluvia")
        if  no_rain.is_active:
            print("no hay lluvia")
        else:
            print("It's raining - get the washing in!")
            data = {"Alerta": "Lluvia"}
            db.child("BaseDeDatos").child("Sensores").set(data)
            sleep(10)
            data = {"Alerta": "None"}
            db.child("BaseDeDatos").child("Sensores").set(data)
            try:
                port.write(b'Al')
            except:
                print("There is no bluetooth")
            
        
    if modoLlama=="true":
        print("detectara Llama")
        
    sleep(1)
    inicio=1
print("FIN")    
    
