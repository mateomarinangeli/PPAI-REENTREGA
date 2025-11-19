package com.redsismica.demo.domain;

public class ClasificacionSismo {
    private int kmProfundidadDesde;
    private int KmProfundidadHasta;
    private String nombre;
    private int idClasificacion;

    public int getIdClasificacion() {
        return idClasificacion;
    }

    public void setIdClasificacion(int idClasificacion) {
        this.idClasificacion = idClasificacion;
    }

    public int getKmProfundidadDesde() {
        return kmProfundidadDesde;
    }

    public void setKmProfundidadDesde(int kmProfundidadDesde) {
        this.kmProfundidadDesde = kmProfundidadDesde;
    }

    public int getKmProfundidadHasta() {
        return KmProfundidadHasta;
    }

    public void setKmProfundidadHasta(int kmProfundidadHasta) {
        KmProfundidadHasta = kmProfundidadHasta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
