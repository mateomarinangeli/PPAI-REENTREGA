package com.redsismica.demo.domain.state;

import com.redsismica.demo.domain.Empleado;
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

    public List<CambioEstado> rechazar(EventoSismico evento, LocalDateTime fechaHora, Empleado empleado) {
        throw new UnsupportedOperationException("Operación 'rechazar' no soportada en este estado.");
    }

    public void dejarSinRevision() {
        throw new UnsupportedOperationException("Operación 'dejarSinRevision' no soportada en este estado.");
    }

    public List<CambioEstado> confirmar(EventoSismico evento, LocalDateTime fechaHora, Empleado e) {
        throw new UnsupportedOperationException("Operación 'confirmar' no soportada en este estado.");
    }

    public List<CambioEstado> solicitarRevision(EventoSismico evento, LocalDateTime fechaHora, Empleado e) {
        throw new UnsupportedOperationException("Operación 'solicitar revision' no soportada en este estado.");
    }
}

