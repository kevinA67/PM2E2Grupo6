package com.example.pm2e2grupo6.Config;

import java.io.Serializable;

public class Contactos implements Serializable {

    private String id_contacto;
    private String full_name;
    private String telefono;
    private String latitud_gps;
    private String longitud_gps;
    private String video;


    public Contactos()
    {

    }

    public Contactos(String id_contacto, String full_name, String telefono, String latitud_gps, String longitud_gps, String video) {
        this.id_contacto = id_contacto;
        this.full_name = full_name;
        this.telefono = telefono;
        this.latitud_gps = latitud_gps;
        this.longitud_gps = longitud_gps;
        this.video = video;
    }

    public Contactos(String id_contacto,String full_name,String telefono, String latitud_gps, String longitud_gps) {
        this.id_contacto=id_contacto;
        this.full_name = full_name;
        this.telefono=telefono;
        this.latitud_gps=latitud_gps;
        this.longitud_gps=longitud_gps;
    }

    public String getId_contacto() {
        return id_contacto;
    }

    public void setId_contacto(String id_contacto) {
        this.id_contacto = id_contacto;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getLatitud_gps() {
        return latitud_gps;
    }

    public void setLatitud_gps(String latitud_gps) {
        this.latitud_gps = latitud_gps;
    }

    public String getLongitud_gps() {
        return longitud_gps;
    }

    public void setLongitud_gps(String longitud_gps) {
        this.longitud_gps = longitud_gps;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}

