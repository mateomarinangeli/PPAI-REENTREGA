package com.redsismica.demo.persistence.Estado;

import com.redsismica.demo.domain.state.Estado;

import java.util.List;

public interface IIntermediarioBDREstado {
    Estado findById(int id);
    Estado findByNombre(String nombre); // Útil por la restricción UNIQUE
    List<Estado> findAll();
    void insert(Estado estado);
    int findIdByNombre(String nombre);
}