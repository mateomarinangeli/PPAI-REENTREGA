package com.redsismica.demo.persistence.ClasificacionSismo;

import com.redsismica.demo.domain.ClasificacionSismo;

import java.util.List;

public interface IIntermediarioBDRClasificacionSismo {
    ClasificacionSismo findById(int id);
    List<ClasificacionSismo> findAll();
    void insert(ClasificacionSismo clasificacion);
    void update(ClasificacionSismo clasificacion);
}