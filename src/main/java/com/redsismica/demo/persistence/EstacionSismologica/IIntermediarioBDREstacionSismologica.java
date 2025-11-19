package com.redsismica.demo.persistence.EstacionSismologica;

import com.redsismica.demo.domain.EstacionSismologica;

import java.util.List;

public interface IIntermediarioBDREstacionSismologica {
    // PK es TEXT
    EstacionSismologica findById(String codigoEstacion);
    List<EstacionSismologica> findAll();
    void insert(EstacionSismologica estacion);
    void update(EstacionSismologica estacion);
}