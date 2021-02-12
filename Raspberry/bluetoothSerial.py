import serial
import time
try :
    port = serial.Serial("/dev/rfcomm0", baudrate=9600)
except:
    print("No hay bluetooth")

# reading and writing data from and to arduino serially.                                      
# rfcomm0 -> this could be different
while True:
    print ("DIGITAL LOGIC -- > SENDING...")
    try:
        
        port.write(b'Al')
    except:
        print("sensor bluetooth no disponible")
    #rcv = port.readline()
    #if rcv:
    #   print(rcv)
    time.sleep( 3 )