package com.redsismica.demo.domain;

import com.redsismica.demo.domain.state.CambioEstado;
import com.redsismica.demo.domain.state.Estado;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EventoSismico {
    private LocalDateTime fechaHoraOcurrencia;
    private LocalDateTime fechaHoraFin;
    private double latitudEpicentro;
    private double longitudEpicentro;
    private double latitudHipocentro;
    private double longitudHipocentro;
    private double valorMagnitud;
    private int idEvento;
    private List<SerieTemporal> serieTemporal;
    private List<CambioEstado> cambioEstado;
    private Empleado analistaSupervisor;
    private Estado estadoActual;
    private AlcanceSismo alcanceSismo;
    private OrigenDeGeneracion origenGeneracion;
    private MagnitudRichter magnitud;
    private ClasificacionSismo clasificacion;

    public Double getValorMagnitud() {
        return valorMagnitud;
    }

    public void setValorMagnitud(Float valorMagnitud) {
        this.valorMagnitud = valorMagnitud;
    }

    public double getLongitudHipocentro() {
        return longitudHipocentro;
    }

    public void setLongitudHipocentro(double longitudHipocentro) {
        this.longitudHipocentro = longitudHipocentro;
    }

    public double getLatitudHipocentro() {
        return latitudHipocentro;
    }

    public void setLatitudHipocentro(double latitudHipocentro) {
        this.latitudHipocentro = latitudHipocentro;
    }

    public double getLongitudEpicentro() {
        return longitudEpicentro;
    }

    public void setLongitudEpicentro(double longitudEpicentro) {
        this.longitudEpicentro = longitudEpicentro;
    }

    public double getLatitudEpicentro() {
        return latitudEpicentro;
    }

    public void setLatitudEpicentro(double latitudEpicentro) {
        this.latitudEpicentro = latitudEpicentro;
    }

    public void setValorMagnitud(double valorMagnitud) {
        this.valorMagnitud = valorMagnitud;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public LocalDateTime getFechaHoraFin() {
        return fechaHoraFin;
    }

    public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    public LocalDateTime getFechaHoraOcurrencia() {
        return fechaHoraOcurrencia;
    }

    public void setFechaHoraOcurrencia(LocalDateTime fechaHoraOcurrencia) {
        this.fechaHoraOcurrencia = fechaHoraOcurrencia;
    }

    public List<SerieTemporal> getSerieTemporal() {
        return serieTemporal;
    }

    public void setSerieTemporal(List<SerieTemporal> serieTemporal) {
        this.serieTemporal = serieTemporal;
    }

    public List<CambioEstado> getCambioEstado() {
        return cambioEstado;
    }

    public void setCambioEstado(List<CambioEstado> cambioEstado) {
        this.cambioEstado = cambioEstado;
    }

    public Empleado getAnalistaSupervisor() {
        return analistaSupervisor;
    }

    public void setAnalistaSupervisor(Empleado analistaSupervisor) {
        this.analistaSupervisor = analistaSupervisor;
    }

    public Estado getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(Estado estadoActual) {
        this.estadoActual = estadoActual;
    }

    public AlcanceSismo getAlcanceSismo() {
        return alcanceSismo;
    }

    public void setAlcanceSismo(AlcanceSismo alcanceSismo) {
        this.alcanceSismo = alcanceSismo;
    }

    public OrigenDeGeneracion getOrigenGeneracion() {
        return origenGeneracion;
    }

    public void setOrigenGeneracion(OrigenDeGeneracion origenGeneracion) {
        this.origenGeneracion = origenGeneracion;
    }

    public MagnitudRichter getMagnitud() {
        return magnitud;
    }

    public void setMagnitud(MagnitudRichter magnitud) {
        this.magnitud = magnitud;
    }

    public ClasificacionSismo getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(ClasificacionSismo clasificacion) {
        this.clasificacion = clasificacion;
    }

    public boolean esAutoDetectado() {
        return estadoActual.esAutoDetectado();
    }

    public List<CambioEstado> bloquear(LocalDateTime fechaHora) {
        List<CambioEstado> cambiosEstado = estadoActual.bloquear(this, fechaHora);
        return cambiosEstado;
    }

    public List<String> buscarDatosSismicos() {
        List<String> datosSismicos = new ArrayList<>();
        datosSismicos.add(alcanceSismo.getNombre());
        datosSismicos.add(clasificacion.getNombre());
        datosSismicos.add(origenGeneracion.getNombre());
        return datosSismicos;
    }

    public boolean validarEvento() {
        return alcanceSismo != null && magnitud != null && origenGeneracion != null;
    }

    public List<CambioEstado> rechazarEvento(LocalDateTime fechaHoraActual, Empleado empleado) {
        List<CambioEstado> cambiosEstado = estadoActual.rechazar(this, fechaHoraActual, empleado);
        return cambiosEstado;
    }

    public void agregarCambioEstado(CambioEstado nuevoCE) {
        cambioEstado.add(nuevoCE);
    }
}
