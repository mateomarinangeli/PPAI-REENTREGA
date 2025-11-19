package com.redsismica.demo.domain.state;

import com.redsismica.demo.domain.Empleado;
import com.redsismica.demo.domain.EventoSismico;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AutoDetectado extends Estado {

    public AutoDetectado() {
        super("AutoDetectado");
    }

    @Override
    public boolean esAutoDetectado() {
        return true;
    }

    @Override
    public List<CambioEstado> bloquear(EventoSismico eventoSismico, LocalDateTime fechaHora) {
        CambioEstado CEActual = buscarCEActual(eventoSismico);
        CEActual.setFechaHoraFin(fechaHora);
        CambioEstado nuevoCE = new CambioEstado();
        nuevoCE.setFechaHoraInicio(fechaHora);
        BloqueadoEnRevision bloqueado = new BloqueadoEnRevision();
        nuevoCE.setEstado(bloqueado);
        nuevoCE.setEmpleado(eventoSismico.getAnalistaSupervisor());
        eventoSismico.setEstadoActual(bloqueado);
        eventoSismico.agregarCambioEstado(nuevoCE);

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