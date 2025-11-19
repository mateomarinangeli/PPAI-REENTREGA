package com.redsismica.demo.domain.state;

import com.redsismica.demo.domain.EventoSismico;

import java.time.LocalDateTime;
import java.util.List;

public abstract class Estado {

    private String nombre;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Estado(String nombre) {
        this.nombre = nombre;
    }

    // ---- Métodos booleanos: devuelven false ----

    public boolean esAutoConfirmado() {
        return false;
    }

    public boolean esAutoDetectado() {
        return false;
    }

    public boolean esBloqueadoEnRevision() {
        return false;
    }

    public boolean esCerrado() {
        return false;
    }

    public boolean esConfirmado() {
        return false;
    }

    public boolean esEventoSinRevision() {
        return false;
    }

    public boolean esPendienteDeCierre() {
        return false;
    }

    public boolean esPendienteDeRevision() {
        return false;
    }

    public boolean esRechazado() {
        return false;
    }

    // ---- Métodos de acción: tiran excepción por defecto ----

    public List<CambioEstado> bloquear(EventoSismico eventoSismico, LocalDateTime fechaHora) {
        throw new UnsupportedOperationException("Operación 'bloquear' no soportada en este estado.");
    }

    public void cerrar() {
        throw new UnsupportedOperationException("Operación 'cerrar' no soportada en este estado.");
    }

    public void confirmar() {
        throw new UnsupportedOperationException("Operación 'confirmar' no soportada en este estado.");
    }

    public void anular() {
        throw new UnsupportedOperationException("Operación 'anular' no soportada en este estado.");
    }

    public void verificarVentanaTemporal() {
        throw new UnsupportedOperationException("Operación 'verificarVentanaTemporal' no soportada en este estado.");
    }

    public void verificarTiempoAceptacion() {
        throw new UnsupportedOperationException("Operación 'verificarTiempoAceptacion' no soportada en este estado.");
    }

    public List<CambioEstado> rechazar(EventoSismico evento, LocalDateTime fechaHora) {
        throw new UnsupportedOperationException("Operación 'rechazar' no soportada en este estado.");
    }
}

