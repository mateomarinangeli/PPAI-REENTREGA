package com.redsismica.demo.persistence.CambioEstado;

import com.redsismica.demo.domain.state.CambioEstado;

import java.util.List;

public interface IIntermediarioBDRCambioEstado {
    CambioEstado findById(int id);
    List<CambioEstado> findAll();
    void update(CambioEstado cambio);

    // 🎯 Método clave para ensamblar EventoSismico (Historial)
    List<CambioEstado> findAllByEventoId(int eventoId);

    void insert(CambioEstado cambio, int idEvento);
}