
from time import sleep
import Streaming
import os,signal
from os import fork,kill
from signal import signal,SIGINT
from sys import exit
from time import sleep

import pyrebase

global flag
flag=False
global s
s=Streaming




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


def handle_signal(sig, frame):
    global s
   
    print('señall'+str(sig))
    if sig == SIGINT:
        print("dying")
        s.StopStream()
        
        
        
        
global inicioS4
inicioS4=0


def stream_Camara(datos):
      global inicioS4
      global flag
      if inicioS4!=0:
        global modoAlerta
        CMR=datos["data"]
        if CMR=="Streaming":
            print("Streaming")
            flag=False
            CrearFork() 
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
          
          
def CrearFork():
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
        kill(pid,SIGINT)
        
        
firebase = pyrebase.initialize_app(config)
db=firebase.database()
stream_Camara = db.child("BaseDeDatos/Camara").stream(stream_Camara)


print("hola")
while True:
        print("..")
        
        sleep(3)
        