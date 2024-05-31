package com.example.kepler201.SetterandGetter;

public class FacturasnoregistradasNewSANDG {

    String SUC;
    String FOLIO;
    String FECHA;
    String SALDO;
    String MONTO;
    String DDP;
    String PAGODDP;
    String MONTOAPAGAR;
    String VENCE;
    String Comentario;
    String Renglon;
    String facturareg;
    int Seleccionado;

    public FacturasnoregistradasNewSANDG(String SUC, String FOLIO, String FECHA, String SALDO, String MONTO, String DDP, String PAGODDP, String MONTOAPAGAR, String VENCE, String comentario, String renglon, String facturareg, int seleccionado) {
        this.SUC = SUC;
        this.FOLIO = FOLIO;
        this.FECHA = FECHA;
        this.SALDO = SALDO;
        this.MONTO = MONTO;
        this.DDP = DDP;
        this.PAGODDP = PAGODDP;
        this.MONTOAPAGAR = MONTOAPAGAR;
        this.VENCE = VENCE;
        Comentario = comentario;
        Renglon = renglon;
        this.facturareg = facturareg;
        Seleccionado = seleccionado;
    }

    public String getSUC() {
        return SUC;
    }

    public void setSUC(String SUC) {
        this.SUC = SUC;
    }

    public String getFOLIO() {
        return FOLIO;
    }

    public void setFOLIO(String FOLIO) {
        this.FOLIO = FOLIO;
    }

    public String getFECHA() {
        return FECHA;
    }

    public void setFECHA(String FECHA) {
        this.FECHA = FECHA;
    }

    public String getSALDO() {
        return SALDO;
    }

    public void setSALDO(String SALDO) {
        this.SALDO = SALDO;
    }

    public String getMONTO() {
        return MONTO;
    }

    public void setMONTO(String MONTO) {
        this.MONTO = MONTO;
    }

    public String getDDP() {
        return DDP;
    }

    public void setDDP(String DDP) {
        this.DDP = DDP;
    }

    public String getPAGODDP() {
        return PAGODDP;
    }

    public void setPAGODDP(String PAGODDP) {
        this.PAGODDP = PAGODDP;
    }

    public String getMONTOAPAGAR() {
        return MONTOAPAGAR;
    }

    public void setMONTOAPAGAR(String MONTOAPAGAR) {
        this.MONTOAPAGAR = MONTOAPAGAR;
    }

    public String getVENCE() {
        return VENCE;
    }

    public void setVENCE(String VENCE) {
        this.VENCE = VENCE;
    }

    public String getComentario() {
        return Comentario;
    }

    public void setComentario(String comentario) {
        Comentario = comentario;
    }

    public String getRenglon() {
        return Renglon;
    }

    public void setRenglon(String renglon) {
        Renglon = renglon;
    }

    public String getFacturareg() {
        return facturareg;
    }

    public void setFacturareg(String facturareg) {
        this.facturareg = facturareg;
    }

    public int getSeleccionado() {
        return Seleccionado;
    }

    public void setSeleccionado(int seleccionado) {
        Seleccionado = seleccionado;
    }
}
