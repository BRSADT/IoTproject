package com.example.iotproyecto;

import java.time.chrono.ThaiBuddhistChronology;

public class Usuario {


    String nombre;
    String ID;
    String Correo;

    Usuario(String Name,String Correo){
        nombre=Name;
        this.Correo=Correo;
    }

    public String getCorreo() {
        return Correo;
    }

    public String getID() {
        return ID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
