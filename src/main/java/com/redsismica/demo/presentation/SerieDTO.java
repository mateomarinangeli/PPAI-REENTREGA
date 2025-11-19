package com.redsismica.demo.presentation;

import java.util.List;

public class SerieDTO {
    public int idSerie;
    public List<MuestraDTO> muestras;

    public int getIdSerie() {
        return idSerie;
    }

    public void setIdSerie(int idSerie) {
        this.idSerie = idSerie;
    }

    public List<MuestraDTO> getMuestras() {
        return muestras;
    }

    public void setMuestras(List<MuestraDTO> muestras) {
        this.muestras = muestras;
    }
}