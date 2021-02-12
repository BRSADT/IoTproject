import os,signal
from time import sleep
import Streaming
import picamera
import signal
import sys

def signal_handler(sig, frame):
    print('señall')
    sys.exit(0)
signal.signal(signal.SIGINT, signal_handler)



pid=os.fork()


 
if pid :
    
    print("proceso padre")
   
    print("proceso padre2")
    sleep(3)
    os.kill(pid,signal.SIGSTOP)
    print("hijo detenido")
    sleep(5)
    info = os.waitpid(pid, os.WSTOPPED)
    stopSignal = os.WSTOPSIG(info[1])
    
    os.kill(pid,signal.SIGCONT)
    print("SEñAL ENVIADA")

else:
    print("proceso hijo")
    print("ProcessID",os.getpid())
    while True:
        print("-------")
        sleep(1)