package com.redsismica.demo.persistence.OrigenDeGeneracion;

import com.redsismica.demo.domain.OrigenDeGeneracion;

import java.util.List;

public interface IIntermediarioBDROrigenGeneracion {
    OrigenDeGeneracion findById(int id);
    List<OrigenDeGeneracion> findAll();
    void insert(OrigenDeGeneracion origen);
    void update(OrigenDeGeneracion origen);
}