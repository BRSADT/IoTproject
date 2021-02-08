package com.example.iotproyecto;

public class ConfigSensores {

String nombreFoto;
String modoAlerta;
    String AlertaLluvia;
    String AlertaLlama;
String segundos;
String ruta;
String IdFoto;
    ConfigSensores(String n,String modo,String segundos,String ruta,String id,String llama,String Lluvia){
        this.nombreFoto=n;
        this.modoAlerta=modo;
        this.segundos=segundos;
        this.ruta=ruta;
        this.IdFoto=id;
        this.AlertaLlama=llama;
        this.AlertaLluvia=Lluvia;
    }



    ConfigSensores(String modo,String segundos,String llama,String Lluvia){
        this.AlertaLlama=llama;
        this.AlertaLluvia=Lluvia;
        this.modoAlerta=modo;
        this.segundos=segundos;
     }

    public String getAlertaLlama() {
        return AlertaLlama;
    }

    public String getAlertaLluvia() {
        return AlertaLluvia;
    }

    public String getIdFoto() {
        return IdFoto;
    }

    public void setIdFoto(String idFoto) {
        IdFoto = idFoto;
    }

    public void setAlertaLlama(String alertaLlama) {
        AlertaLlama = alertaLlama;
    }

    public void setAlertaLluvia(String alertaLluvia) {
        AlertaLluvia = alertaLluvia;
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
