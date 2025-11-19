package com.redsismica.demo.domain;

public class AlcanceSismo {
    private String descripcion;
    private String nombre;
    private int idAlcance;

    public int getIdAlcance() {
        return idAlcance;
    }

    public void setIdAlcance(int idAlcance) {
        this.idAlcance = idAlcance;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
