package com.redsismica.demo.persistence.Usuario;

import com.redsismica.demo.domain.Usuario;

import java.util.List;

public interface IIntermediarioBDRUsuario {
    Usuario findById(int id);
    Usuario findByNombreUsuario(String nombreUsuario); // Útil por la restricción UNIQUE
    List<Usuario> findAll();

    // Método para buscar el usuario asociado a un empleado (FK)
    //Usuario findByEmpleadoId(int empleadoId);

    void insert(Usuario usuario);
    void update(Usuario usuario);
}