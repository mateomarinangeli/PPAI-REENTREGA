package com.redsismica.demo.domain;

public class DetalleMuestraSismica {
    private double valor;
    private TipoDeDato tipoDeDato;
    private int idDetalleMuestra;

    public int getIdDetalleMuestra() {
        return idDetalleMuestra;
    }

    public void setIdDetalleMuestra(int idDetalleMuestra) {
        this.idDetalleMuestra = idDetalleMuestra;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public TipoDeDato getTipoDeDato() {
        return tipoDeDato;
    }

    public void setTipoDeDato(TipoDeDato tipoDeDato) {
        this.tipoDeDato = tipoDeDato;
    }
}
