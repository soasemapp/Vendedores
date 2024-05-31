package com.example.kepler201.SetterandGetter;

public class RegistroPagosNewSANDG {

    String Fecha;
    String Hora;
    String Vendedor;
    String ClaveCliente;
    String Nombre;
    String Facturas;
    String Importe;
    String Banco;
    String BancoNombre;
    String formadepago;
    String com1;
    String com2;
    String com3;


    public RegistroPagosNewSANDG(String fecha, String hora, String vendedor, String claveCliente, String nombre, String facturas, String importe, String banco, String bancoNombre, String formadepago, String com1, String com2, String com3) {
        Fecha = fecha;
        Hora = hora;
        Vendedor = vendedor;
        ClaveCliente = claveCliente;
        Nombre = nombre;
        Facturas = facturas;
        Importe = importe;
        Banco = banco;
        BancoNombre = bancoNombre;
        this.formadepago = formadepago;
        this.com1 = com1;
        this.com2 = com2;
        this.com3 = com3;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getHora() {
        return Hora;
    }

    public void setHora(String hora) {
        Hora = hora;
    }

    public String getVendedor() {
        return Vendedor;
    }

    public void setVendedor(String vendedor) {
        Vendedor = vendedor;
    }

    public String getClaveCliente() {
        return ClaveCliente;
    }

    public void setClaveCliente(String claveCliente) {
        ClaveCliente = claveCliente;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getFacturas() {
        return Facturas;
    }

    public void setFacturas(String facturas) {
        Facturas = facturas;
    }

    public String getImporte() {
        return Importe;
    }

    public void setImporte(String importe) {
        Importe = importe;
    }

    public String getBanco() {
        return Banco;
    }

    public void setBanco(String banco) {
        Banco = banco;
    }

    public String getBancoNombre() {
        return BancoNombre;
    }

    public void setBancoNombre(String bancoNombre) {
        BancoNombre = bancoNombre;
    }

    public String getFormadepago() {
        return formadepago;
    }

    public void setFormadepago(String formadepago) {
        this.formadepago = formadepago;
    }

    public String getCom1() {
        return com1;
    }

    public void setCom1(String com1) {
        this.com1 = com1;
    }

    public String getCom2() {
        return com2;
    }

    public void setCom2(String com2) {
        this.com2 = com2;
    }

    public String getCom3() {
        return com3;
    }

    public void setCom3(String com3) {
        this.com3 = com3;
    }
}
