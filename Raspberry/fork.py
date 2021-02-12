import os,signal
from time import sleep
import Streaming
import picamera
camera=picamera.PiCamera()

pid=os.fork()
global s
s=Streaming 

 
if pid :
    
    print("proceso padre")
    sleep(20)
    print("proceso padre2")
    s.StopStream(camera)
    sleep(5)
    os.kill(pid,signal.SIGSTOP)
    print("signal sent")
    info = os.waitpid(pid, os.WSTOPPED)
    stopSignal = os.WSTOPSIG(info[1])
    print("child stopped")
    os.kill(pid,signal.SIGCONT)
    print("SEñAL ENVIADA")

else:
    print("proceso hijo")
    print("ProcessID",os.getpid())
    s.StartStream(camera)
    print("-------")
