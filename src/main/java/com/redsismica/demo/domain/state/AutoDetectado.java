package com.redsismica.demo.domain.state;

public class AutoDetectado extends Estado {

    public AutoDetectado() {
        super("AutoDetectado");
    }

    @Override
    public boolean esAutoDetectado() {
        return true;
    }
}