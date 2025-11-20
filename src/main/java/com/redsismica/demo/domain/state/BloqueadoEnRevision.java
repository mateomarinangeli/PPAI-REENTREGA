package com.redsismica.demo.domain.state;

import com.redsismica.demo.domain.Empleado;
import com.redsismica.demo.domain.EventoSismico;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BloqueadoEnRevision extends Estado {

    public BloqueadoEnRevision() {
        super("BloqueadoEnRevision");
    }

    @Override
    public List<CambioEstado> rechazar(EventoSismico evento, LocalDateTime fechaHora, Empleado empleado) {
        CambioEstado CEActual = buscarCEActual(evento);
        CEActual.setFechaHoraFin(fechaHora);
        CambioEstado nuevoCE = new CambioEstado();
        nuevoCE.setFechaHoraInicio(fechaHora);
        Rechazado rechazado = new Rechazado();
        nuevoCE.setEstado(rechazado);
        nuevoCE.setEmpleado(empleado);
        evento.setEstadoActual(rechazado);
        evento.agregarCambioEstado(nuevoCE);

        List<CambioEstado> cambiosEstado = new ArrayList<>();
        cambiosEstado.add(CEActual);
        cambiosEstado.add(nuevoCE);
        return cambiosEstado;
    }

    private CambioEstado buscarCEActual(EventoSismico eventoSismico) {
        if (eventoSismico == null || eventoSismico.getCambioEstado() == null) {
            return null; // o lanzar excepción, según tu diseño
        }

        for (CambioEstado ce : eventoSismico.getCambioEstado()) {
            if (ce.getFechaHoraFin() == null) {
                return ce;   // este es el cambio de estado actual
            }
        }

        return null; // si no encontró ninguno abierto
    }

    @Override
    public List<CambioEstado> confirmar(EventoSismico evento, LocalDateTime fechaHora, Empleado empleado) {
        CambioEstado CEActual = buscarCEActual(evento);
        CEActual.setFechaHoraFin(fechaHora);
        CambioEstado nuevoCE = new CambioEstado();
        nuevoCE.setFechaHoraInicio(fechaHora);
        Confirmado confirmado = new Confirmado();
        nuevoCE.setEstado(confirmado);
        nuevoCE.setEmpleado(empleado);
        evento.setEstadoActual(confirmado);
        evento.agregarCambioEstado(nuevoCE);

        List<CambioEstado> cambiosEstado = new ArrayList<>();
        cambiosEstado.add(CEActual);
        cambiosEstado.add(nuevoCE);
        return cambiosEstado;
    }

    @Override
    public List<CambioEstado> solicitarRevision(EventoSismico evento, LocalDateTime fechaHora, Empleado empleado) {
        // 1. Cerrar el cambio de estado actual
        CambioEstado CEActual = buscarCEActual(evento);
        CEActual.setFechaHoraFin(fechaHora);
        CambioEstado nuevoCE = new CambioEstado();
        nuevoCE.setFechaHoraInicio(fechaHora);
        SolicitudRevisionExperto revisionExperto = new SolicitudRevisionExperto();
        nuevoCE.setEstado(revisionExperto);
        nuevoCE.setEmpleado(empleado);
        evento.setEstadoActual(revisionExperto);
        evento.agregarCambioEstado(nuevoCE);

        List<CambioEstado> cambiosEstado = new ArrayList<>();
        cambiosEstado.add(CEActual);
        cambiosEstado.add(nuevoCE);
        return cambiosEstado;
    }
}