package com.example.kepler201.SetterandGetter;

public class ListPrecSANDG {

    String CodeProdu;
    String NomProd;
    String CodBarras;
    String Existencia1;
    String Importe;
    String url;

    public ListPrecSANDG(String codeProdu, String nomProd, String codBarras, String existencia1, String importe, String url) {
        CodeProdu = codeProdu;
        NomProd = nomProd;
        CodBarras = codBarras;
        Existencia1 = existencia1;
        Importe = importe;
        this.url = url;
    }

    public String getCodeProdu() {
        return CodeProdu;
    }

    public void setCodeProdu(String codeProdu) {
        CodeProdu = codeProdu;
    }

    public String getNomProd() {
        return NomProd;
    }

    public void setNomProd(String nomProd) {
        NomProd = nomProd;
    }

    public String getCodBarras() {
        return CodBarras;
    }

    public void setCodBarras(String codBarras) {
        CodBarras = codBarras;
    }

    public String getExistencia1() {
        return Existencia1;
    }

    public void setExistencia1(String existencia1) {
        Existencia1 = existencia1;
    }

    public String getImporte() {
        return Importe;
    }

    public void setImporte(String importe) {
        Importe = importe;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}