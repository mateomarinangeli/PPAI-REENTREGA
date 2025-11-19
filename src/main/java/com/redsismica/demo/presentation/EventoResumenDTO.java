package com.redsismica.demo.presentation;

public class EventoResumenDTO {
    public int idEvento;
    public String fechaHora; // O un objeto Date/Time
    public double latEpicentro;
    public double lonEpicentro;
    public double latHipocentro;
    public double lonHipocentro;
    public double valorMagnitud;

    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public double getLatEpicentro() {
        return latEpicentro;
    }

    public void setLatEpicentro(double latEpicentro) {
        this.latEpicentro = latEpicentro;
    }

    public double getLonEpicentro() {
        return lonEpicentro;
    }

    public void setLonEpicentro(double lonEpicentro) {
        this.lonEpicentro = lonEpicentro;
    }

    public double getLatHipocentro() {
        return latHipocentro;
    }

    public void setLatHipocentro(double latHipocentro) {
        this.latHipocentro = latHipocentro;
    }

    public double getLonHipocentro() {
        return lonHipocentro;
    }

    public void setLonHipocentro(double lonHipocentro) {
        this.lonHipocentro = lonHipocentro;
    }

    public double getValorMagnitud() {
        return valorMagnitud;
    }

    public void setValorMagnitud(double valorMagnitud) {
        this.valorMagnitud = valorMagnitud;
    }
}