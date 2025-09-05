package com.example.kepler201.SetterandGetter;

public class VentasDevSANDG {
    String SucursalVeDe;
    String FolioVeDe;
    String CantidadVeDe;
    String FechaVeDe;
    String TipoVeDe;

    public VentasDevSANDG(String sucursalVeDe, String folioVeDe, String cantidadVeDe, String fechaVeDe, String tipoVeDe) {
        SucursalVeDe = sucursalVeDe;
        FolioVeDe = folioVeDe;
        CantidadVeDe = cantidadVeDe;
        FechaVeDe = fechaVeDe;
        TipoVeDe = tipoVeDe;
    }

    public String getSucursalVeDe() {
        return SucursalVeDe;
    }

    public void setSucursalVeDe(String sucursalVeDe) {
        SucursalVeDe = sucursalVeDe;
    }

    public String getFolioVeDe() {
        return FolioVeDe;
    }

    public void setFolioVeDe(String folioVeDe) {
        FolioVeDe = folioVeDe;
    }

    public String getCantidadVeDe() {
        return CantidadVeDe;
    }

    public void setCantidadVeDe(String cantidadVeDe) {
        CantidadVeDe = cantidadVeDe;
    }

    public String getFechaVeDe() {
        return FechaVeDe;
    }

    public void setFechaVeDe(String fechaVeDe) {
        FechaVeDe = fechaVeDe;
    }

    public String getTipoVeDe() {
        return TipoVeDe;
    }

    public void setTipoVeDe(String tipoVeDe) {
        TipoVeDe = tipoVeDe;
    }
}
