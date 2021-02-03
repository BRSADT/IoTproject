package com.example.iotproyecto;

public class ConfigSensores {

String nombreFoto;
String modoAlerta;
String segundos;
String ruta;

    ConfigSensores(String n,String modo,String segundos,String ruta){
        this.nombreFoto=n;
        this.modoAlerta=modo;
        this.segundos=segundos;
        this.ruta=ruta;
    }

    ConfigSensores(String modo,String segundos){

        this.modoAlerta=modo;
        this.segundos=segundos;
     }

    public String getNombreFoto() {
        return nombreFoto;
    }

    public void setNombreFoto(String nombreFoto) {
        this.nombreFoto = nombreFoto;
    }

    public String getModoAlerta() {
        return modoAlerta;
    }

    public void setModoAlerta(String modoAlerta) {
        this.modoAlerta = modoAlerta;
    }

    public String getSegundos() {
        return segundos;
    }

    public void setSegundos(String segundos) {
        this.segundos = segundos;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
}
