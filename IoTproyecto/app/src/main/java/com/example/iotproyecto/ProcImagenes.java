package com.example.iotproyecto;

public class ProcImagenes {
    String nameUID;
    String nombreArchivo;

    ProcImagenes(String nameUID, String nombreArchivo){
        this.nameUID=nameUID;
        this.nombreArchivo=nombreArchivo;

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
}
