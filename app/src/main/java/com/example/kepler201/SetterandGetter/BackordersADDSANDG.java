package com.example.kepler201.SetterandGetter;

public class BackordersADDSANDG {
    String Folio;
    String Producto;
    String Descripcion;
    String BackOrder;
    String Existencia;

    public BackordersADDSANDG(String folio, String producto, String descripcion, String backOrder, String existencia) {
        Folio = folio;
        Producto = producto;
        Descripcion = descripcion;
        BackOrder = backOrder;
        Existencia = existencia;
    }

    public String getFolio() {
        return Folio;
    }

    public void setFolio(String folio) {
        Folio = folio;
    }

    public String getProducto() {
        return Producto;
    }

    public void setProducto(String producto) {
        Producto = producto;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getBackOrder() {
        return BackOrder;
    }

    public void setBackOrder(String backOrder) {
        BackOrder = backOrder;
    }

    public String getExistencia() {
        return Existencia;
    }

    public void setExistencia(String existencia) {
        Existencia = existencia;
    }
}
