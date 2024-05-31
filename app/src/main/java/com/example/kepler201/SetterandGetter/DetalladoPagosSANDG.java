package com.example.kepler201.SetterandGetter;

public class DetalladoPagosSANDG {

    String sucursal;
    String folio;
    String monto;
    String montoapagar;
    String comentario;

    public DetalladoPagosSANDG(String sucursal, String folio, String monto, String montoapagar, String comentario) {
        this.sucursal = sucursal;
        this.folio = folio;
        this.monto = monto;
        this.montoapagar = montoapagar;
        this.comentario = comentario;
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

    public String getMontoapagar() {
        return montoapagar;
    }

    public void setMontoapagar(String montoapagar) {
        this.montoapagar = montoapagar;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
