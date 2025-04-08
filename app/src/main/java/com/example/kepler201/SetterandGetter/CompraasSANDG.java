package com.example.kepler201.SetterandGetter;

public class CompraasSANDG {
   String Folio;
   String Fecha;
   String Cantidad;
   String Sucursal;
   String Nombre;

    public CompraasSANDG(String folio, String fecha, String cantidad, String sucursal, String nombre) {
        Folio = folio;
        Fecha = fecha;
        Cantidad = cantidad;
        Sucursal = sucursal;
        Nombre = nombre;
    }

    public String getFolio() {
        return Folio;
    }

    public void setFolio(String folio) {
        Folio = folio;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getCantidad() {
        return Cantidad;
    }

    public void setCantidad(String cantidad) {
        Cantidad = cantidad;
    }

    public String getSucursal() {
        return Sucursal;
    }

    public void setSucursal(String sucursal) {
        Sucursal = sucursal;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }
}
