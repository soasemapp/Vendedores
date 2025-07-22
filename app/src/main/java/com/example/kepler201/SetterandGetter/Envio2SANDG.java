package com.example.kepler201.SetterandGetter;

import java.io.Serializable;
import java.util.Objects;

public class Envio2SANDG implements Serializable {

    String id;
    String Direccion;
    String latitud;
    String longitud;

    private boolean visitado = false;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Envio2SANDG that = (Envio2SANDG) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(latitud, that.latitud) &&
                Objects.equals(longitud, that.longitud);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, latitud, longitud);
    }




}
