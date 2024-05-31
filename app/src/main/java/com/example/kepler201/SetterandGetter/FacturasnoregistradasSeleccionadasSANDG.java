package com.example.kepler201.SetterandGetter;

public class FacturasnoregistradasSeleccionadasSANDG {

    String cliente;
    String vendedor;
    String sucursal;
    String folio;
    String monto;
    String MONTOAPAGAR;
    String comentario;

    public FacturasnoregistradasSeleccionadasSANDG(String cliente, String vendedor, String sucursal, String folio, String monto, String MONTOAPAGAR, String comentario) {
        this.cliente = cliente;
        this.vendedor = vendedor;
        this.sucursal = sucursal;
        this.folio = folio;
        this.monto = monto;
        this.MONTOAPAGAR = MONTOAPAGAR;
        this.comentario = comentario;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getMONTOAPAGAR() {
        return MONTOAPAGAR;
    }

    public void setMONTOAPAGAR(String MONTOAPAGAR) {
        this.MONTOAPAGAR = MONTOAPAGAR;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
