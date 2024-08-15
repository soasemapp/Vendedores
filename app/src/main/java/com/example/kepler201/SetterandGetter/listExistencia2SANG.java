package com.example.kepler201.SetterandGetter;

public class listExistencia2SANG {
    String  ClaveSu;
    String  Nombre;
    String Existencia;
    String ClaveProdu;
    String ClaveCompetecia;
    String  Descr;
    String CodBarras;
    String Precio;
    String FotosTipo;
    String FotosLinea;

    public listExistencia2SANG(String claveSu, String nombre, String existencia, String claveProdu, String claveCompetecia, String descr, String codBarras, String precio, String fotosTipo, String fotosLinea) {
        ClaveSu = claveSu;
        Nombre = nombre;
        Existencia = existencia;
        ClaveProdu = claveProdu;
        ClaveCompetecia = claveCompetecia;
        Descr = descr;
        CodBarras = codBarras;
        Precio = precio;
        FotosTipo = fotosTipo;
        FotosLinea = fotosLinea;
    }

    public String getClaveSu() {
        return ClaveSu;
    }

    public void setClaveSu(String claveSu) {
        ClaveSu = claveSu;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getExistencia() {
        return Existencia;
    }

    public void setExistencia(String existencia) {
        Existencia = existencia;
    }

    public String getClaveProdu() {
        return ClaveProdu;
    }

    public void setClaveProdu(String claveProdu) {
        ClaveProdu = claveProdu;
    }

    public String getClaveCompetecia() {
        return ClaveCompetecia;
    }

    public void setClaveCompetecia(String claveCompetecia) {
        ClaveCompetecia = claveCompetecia;
    }

    public String getDescr() {
        return Descr;
    }

    public void setDescr(String descr) {
        Descr = descr;
    }

    public String getCodBarras() {
        return CodBarras;
    }

    public void setCodBarras(String codBarras) {
        CodBarras = codBarras;
    }

    public String getPrecio() {
        return Precio;
    }

    public void setPrecio(String precio) {
        Precio = precio;
    }

    public String getFotosTipo() {
        return FotosTipo;
    }

    public void setFotosTipo(String fotosTipo) {
        FotosTipo = fotosTipo;
    }

    public String getFotosLinea() {
        return FotosLinea;
    }

    public void setFotosLinea(String fotosLinea) {
        FotosLinea = fotosLinea;
    }
}