# Web streaming example
# Source code from the official PiCamera package
# http://picamera.readthedocs.io/en/latest/recipes2.html#web-streaming

import io

import logging
import socketserver
from threading import Condition
from http import server
from time import sleep

import picamera

import pyrebase
global camera

global server


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


inicioS4=0

PAGE="""\
<html>
<head>
<title>Raspberry Pi - Surveillance Camera</title>
</head>
<body>
<center><h1>Raspberry Pi - Surveillance Camera</h1></center>
<center><img src="stream.mjpg" width="640" height="480"></center>
</body>
</html>
"""


def stream_Camara(datos):
      global inicioS4
      if inicioS4!=0:
         # {'title': 'Pyrebase', "body": "etc..."}
        global modoAlerta
        CMR=datos["data"]
        if CMR=="Streaming":
            #StartStream()
            print("Streaming")
            
        else:
              StopStream()
              print("No Streaming")
                
      else:
          inicioS4=1 

def StopStream():
    global camera
    print("se detendra")
    camera.close()
    #server.shutdown()
    #camera.stop_recording()
     

def StartStream():
    global camera
    global server
    print("--->start Streaming")
    #with picamera.PiCamera(resolution='640x480', framerate=24) as camera:
    camera=picamera.PiCamera()        
    global output
    output= StreamingOutput()
    #Uncomment the next line to change your Pi's Camera rotation (in degrees)
    #camera.rotation = 90
    
    camera.start_recording( output, format='mjpeg')
    try:
        
        print("aqui") 
        server.serve_forever()
         
    finally:
        print("terminara")
        server.shutdown()
        server.serve_close()
        
                        



class StreamingOutput(object):
    def __init__(self):
        self.frame = None
        self.buffer = io.BytesIO()
        self.condition = Condition()

    def write(self, buf):
        if buf.startswith(b'\xff\xd8'):
            # New frame, copy the existing buffer's content and notify all
            # clients it's available
            self.buffer.truncate()
            with self.condition:
                self.frame = self.buffer.getvalue()
                self.condition.notify_all()
            self.buffer.seek(0)
        return self.buffer.write(buf)

class StreamingHandler(server.BaseHTTPRequestHandler):
    def do_GET(self):
        global output 
        if self.path == '/':
            self.send_response(301)
            self.send_header('Location', '/index.html')
            self.end_headers()
        elif self.path == '/index.html':
            content = PAGE.encode('utf-8')
            self.send_response(200)
            self.send_header('Content-Type', 'text/html')
            self.send_header('Content-Length', len(content))
            self.end_headers()
            self.wfile.write(content)
        elif self.path == '/stream.mjpg':
            self.send_response(200)
            self.send_header('Age', 0)
            self.send_header('Cache-Control', 'no-cache, private')
            self.send_header('Pragma', 'no-cache')
            self.send_header('Content-Type', 'multipart/x-mixed-replace; boundary=FRAME')
            self.end_headers()
            try:
                while True:
                    with output.condition:
                         output.condition.wait()
                         frame = output.frame
                    self.wfile.write(b'--FRAME\r\n')
                    self.send_header('Content-Type', 'image/jpeg')
                    self.send_header('Content-Length', len(frame))
                    self.end_headers()
                    self.wfile.write(frame)
                    self.wfile.write(b'\r\n')
            except Exception as e:
                logging.warning(
                    'Removed streaming client %s: %s',
                    self.client_address, str(e))
        else:
            self.send_error(404)
            self.end_headers()

class StreamingServer(socketserver.ThreadingMixIn, server.HTTPServer):
    allow_reuse_address = True
    daemon_threads = True
    bind_and_activate=False
firebase = pyrebase.initialize_app(config)
db=firebase.database()
    
#stream_Camara = db.child("BaseDeDatos/Camara").stream(stream_Camara)
address = ('', 8000)
server = StreamingServer(address, StreamingHandler)
print("aqui")
#sleep(10)
#tartStream()
print("finalizo")        