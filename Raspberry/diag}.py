class Persona():
    def __init__(self,nombre,edad,calificacion):
        self.edad=edad
        self.nombre=nombre
        self.calificacion=calificacion
        
    def regresar(self):
        lista=[self.edad,self.nombre,self.calificacion]
        return lista
    
Brenda=Persona("brenda",22,100);
l=Brenda.regresar()
print(l)
      