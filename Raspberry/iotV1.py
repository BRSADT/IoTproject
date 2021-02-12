from picamera import PiCamera
from time import sleep
from datetime import datetime
import pyrebase
import boto3
import os 
from botocore.exceptions import ClientError


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
i=0
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

def stream_ProcesarImagenes(datos):
    if i!=0:
        print(datos["data"]) # {'title': 'Pyrebase', "body": "etc..."}
       
        #datos=message["data"]['Usuario'];
        #print(datos)
        """
        path=datos["data"]['nombre']
        print("Path "+path)
        UID=datos["data"]['nameUID']
        print("usuario "+UID)
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
"""
camera = PiCamera()
firebase = pyrebase.initialize_app(config)
db=firebase.database()
my_stream = db.child("BaseDeDatos/SolicitudImagenes").stream(stream_ProcesarImagenes)
storage = firebase.storage()




while True:
    camera.start_preview()
    sleep(5)
    now=datetime.now()
    titulo =now.strftime("%d%m%Y%H%M");
    #camera.capture('/home/pi/Desktop/imgCamara/%s.jpg'%titulo)
    #camera.stop_preview
    
    
    #storage.child("/Desktop/%s.jpg"%titulo).put("imgCamara/%s.jpg"%titulo)   
    print("%s"% i)
    i=i+1
    sleep(60)
    
print("FIN")   