package com.redsismica.demo.domain;

public class MagnitudRichter {
    private String descripcionMagnitud;
    private double numero;
    private int idMagnitud;

    public int getIdMagnitud() {
        return idMagnitud;
    }

    public void setIdMagnitud(int idMagnitud) {
        this.idMagnitud = idMagnitud;
    }

    public double getNumero() {
        return numero;
    }

    public void setNumero(double numero) {
        this.numero = numero;
    }

    public String getDescripcionMagnitud() {
        return descripcionMagnitud;
    }

    public void setDescripcionMagnitud(String descripcionMagnitud) {
        this.descripcionMagnitud = descripcionMagnitud;
    }
}
