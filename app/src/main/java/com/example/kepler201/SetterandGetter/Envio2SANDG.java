package com.example.kepler201.SetterandGetter;

public class Envio2SANDG {

    String id;
    String Direccion;
    String latitud;
    String longitud;

    public Envio2SANDG(String id, String direccion, String latitud, String longitud) {
        this.id = id;
        Direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
}
