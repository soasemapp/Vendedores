package com.example.kepler201.SetterandGetter;

public class ProductosNuevosSANDG {
    String clave;
    String Descripcion;
    String Tipo;
    String Url;

    public ProductosNuevosSANDG(String clave, String descripcion, String tipo, String url) {
        this.clave = clave;
        Descripcion = descripcion;
        Tipo = tipo;
        Url = url;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
