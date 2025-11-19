package com.redsismica.demo.persistence.Sesion;

import com.redsismica.demo.domain.Sesion;

import java.util.List;

public interface IIntermediarioBDRSesion {
    Sesion findById(int id);
    List<Sesion> findAll();

    // Método para buscar sesiones de un usuario (FK)
    //List<Sesion> findAllByUsuarioId(int usuarioId);

    List<Sesion> findAllByUsuarioId(int usuarioId);

    void insert(Sesion sesion);
    void update(Sesion sesion);
}