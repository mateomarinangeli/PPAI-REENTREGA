package com.redsismica.demo.persistence.Empleado;

import com.redsismica.demo.domain.Empleado;

import java.util.List;

public interface IIntermediarioBDREmpleado {
    Empleado findById(int id);
    List<Empleado> findAll();
    void insert(Empleado empleado);
    void update(Empleado empleado);
}