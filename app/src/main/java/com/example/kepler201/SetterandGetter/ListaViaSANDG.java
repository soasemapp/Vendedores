package com.example.kepler201.SetterandGetter;

public class ListaViaSANDG {

    String Clave;
    String Nombre;
    String Monto;
    String porcentaje;
    String entregadirecta;

    public ListaViaSANDG(String clave, String nombre, String monto, String porcentaje, String entregadirecta) {
        Clave = clave;
        Nombre = nombre;
        Monto = monto;
        this.porcentaje = porcentaje;
        this.entregadirecta = entregadirecta;
    }

    public String getClave() {
        return Clave;
    }

    public void setClave(String clave) {
        Clave = clave;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getMonto() {
        return Monto;
    }

    public void setMonto(String monto) {
        Monto = monto;
    }

    public String getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(String porcentaje) {
        this.porcentaje = porcentaje;
    }

    public String getEntregadirecta() {
        return entregadirecta;
    }

    public void setEntregadirecta(String entregadirecta) {
        this.entregadirecta = entregadirecta;
    }
}
