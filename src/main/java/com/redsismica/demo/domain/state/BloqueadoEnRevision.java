package com.redsismica.demo.domain.state;

import com.redsismica.demo.domain.EventoSismico;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BloqueadoEnRevision extends Estado {

    public BloqueadoEnRevision() {
        super("BloqueadoEnRevision");
    }

    @Override
    public List<CambioEstado> rechazar(EventoSismico evento, LocalDateTime fechaHora) {
        CambioEstado CEActual = buscarCEActual(evento);
        CEActual.setFechaHoraFin(fechaHora);
        CambioEstado nuevoCE = new CambioEstado();
        nuevoCE.setFechaHoraInicio(fechaHora);
        Rechazado rechazado = new Rechazado();
        nuevoCE.setEstado(rechazado);
        nuevoCE.setEmpleado(evento.getAnalistaSupervisor());
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
}