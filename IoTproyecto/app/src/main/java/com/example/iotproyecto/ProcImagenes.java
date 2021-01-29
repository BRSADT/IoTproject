package com.example.iotproyecto;

public class ProcImagenes {
    String nameUID;
    String nombreArchivo;
    String ID;
    ProcImagenes(String nameUID, String nombreArchivo,String ID){
        this.nameUID=nameUID;
        this.nombreArchivo=nombreArchivo;
        this.ID=ID;
    }
    public void setNombre(String nombre) {
        this.nombreArchivo = nombre;
    }

    public void setNameUID(String nameUID) {
        this.nameUID = nameUID;
    }

    public String getNombre() {
        return nombreArchivo;
    }

    public String getNameUID() {
        return nameUID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
