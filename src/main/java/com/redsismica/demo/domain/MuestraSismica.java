package com.redsismica.demo.domain;

import java.time.LocalDateTime;
import java.util.List;

public class MuestraSismica {
    private LocalDateTime fechaHoraMuestra;
    private List<DetalleMuestraSismica> detalleMuestraSismica;
    private int idMuestraSismica;

    public int getIdMuestraSismica() {
        return idMuestraSismica;
    }

    public void setIdMuestraSismica(int idMuestraSismica) {
        this.idMuestraSismica = idMuestraSismica;
    }

    public LocalDateTime getFechaHoraMuestra() {
        return fechaHoraMuestra;
    }

    public void setFechaHoraMuestra(LocalDateTime fechaHoraMuestra) {
        this.fechaHoraMuestra = fechaHoraMuestra;
    }

    public List<DetalleMuestraSismica> getDetalleMuestraSismica() {
        return detalleMuestraSismica;
    }

    public void setDetalleMuestraSismica(List<DetalleMuestraSismica> detalleMuestraSismica) {
        this.detalleMuestraSismica = detalleMuestraSismica;
    }
}
