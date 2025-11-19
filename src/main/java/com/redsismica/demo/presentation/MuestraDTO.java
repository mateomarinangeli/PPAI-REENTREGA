package com.redsismica.demo.presentation;

import java.time.LocalDateTime;
import java.util.List;

public class MuestraDTO {
    public LocalDateTime fechaHoraMuestra;
    public List<DetalleMuestraDTO> detalles;

    public LocalDateTime getFechaHoraMuestra() {
        return fechaHoraMuestra;
    }

    public void setFechaHoraMuestra(LocalDateTime fechaHoraMuestra) {
        this.fechaHoraMuestra = fechaHoraMuestra;
    }

    public List<DetalleMuestraDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleMuestraDTO> detalles) {
        this.detalles = detalles;
    }
}
