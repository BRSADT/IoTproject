from datetime import datetime
now=datetime.now()
tituloFoto =now.strftime("%d%m%Y%H%M");
print("fecha",tituloFoto)
hora=now.strftime("%H:%M");
fecha=now.strftime("%d/%m/%Y");
print("hora",hora)
print("fecha",fecha)
