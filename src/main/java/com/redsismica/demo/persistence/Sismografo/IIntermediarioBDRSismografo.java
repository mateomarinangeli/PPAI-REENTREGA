package com.redsismica.demo.persistence.Sismografo;

import com.redsismica.demo.domain.Sismografo;

import java.util.List;

public interface IIntermediarioBDRSismografo {
    Sismografo findById(int id);
    List<Sismografo> findAll();

    // Método para buscar sismógrafos de una estación específica (FK)
    List<Sismografo> findAllByCodigoEstacion(String codigoEstacion);

    void insert(Sismografo sismografo);
    void update(Sismografo sismografo);
}