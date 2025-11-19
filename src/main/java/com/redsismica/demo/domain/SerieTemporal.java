package com.redsismica.demo.domain;

import java.time.LocalDateTime;
import java.util.List;

public class SerieTemporal {
    private boolean condicionAlarma;
    private LocalDateTime fechaHoraInicioRegistroMuestras;
    private LocalDateTime fechaHoraRegistro;
    private int frecuenciaMuestreo;
    private int idSerieTemporal;
    private List<MuestraSismica> muestrasSismicas;

    public int getIdSerieTemporal() {
        return idSerieTemporal;
    }

    public void setIdSerieTemporal(int idSerieTemporal) {
        this.idSerieTemporal = idSerieTemporal;
    }

    public boolean isCondicionAlarma() {
        return condicionAlarma;
    }

    public void setCondicionAlarma(boolean condicionAlarma) {
        this.condicionAlarma = condicionAlarma;
    }

    public LocalDateTime getFechaHoraInicioRegistroMuestras() {
        return fechaHoraInicioRegistroMuestras;
    }

    public void setFechaHoraInicioRegistroMuestras(LocalDateTime fechaHoraInicioRegistroMuestras) {
        this.fechaHoraInicioRegistroMuestras = fechaHoraInicioRegistroMuestras;
    }

    public LocalDateTime getFechaHoraRegistro() {
        return fechaHoraRegistro;
    }

    public void setFechaHoraRegistro(LocalDateTime fechaHoraRegistro) {
        this.fechaHoraRegistro = fechaHoraRegistro;
    }

    public int getFrecuenciaMuestreo() {
        return frecuenciaMuestreo;
    }

    public void setFrecuenciaMuestreo(int frecuenciaMuestreo) {
        this.frecuenciaMuestreo = frecuenciaMuestreo;
    }

    public List<MuestraSismica> getMuestrasSismicas() {
        return muestrasSismicas;
    }

    public void setMuestrasSismicas(List<MuestraSismica> muestrasSismicas) {
        this.muestrasSismicas = muestrasSismicas;
    }
}
