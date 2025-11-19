package com.redsismica.demo.domain;

public class TipoDeDato {
    private String denominacion;
    private String nombreUnidadMedida;
    private double valorUmbral;
    private int idTipoDeDato;

    public int getIdTipoDeDato() {
        return idTipoDeDato;
    }

    public void setIdTipoDeDato(int idTipoDeDato) {
        this.idTipoDeDato = idTipoDeDato;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public String getNombreUnidadMedida() {
        return nombreUnidadMedida;
    }

    public void setNombreUnidadMedida(String nombreUnidadMedida) {
        this.nombreUnidadMedida = nombreUnidadMedida;
    }

    public double getValorUmbral() {
        return valorUmbral;
    }

    public void setValorUmbral(double valorUmbral) {
        this.valorUmbral = valorUmbral;
    }
}
