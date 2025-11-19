package com.redsismica.demo.persistence.EventoSismico;

import com.redsismica.demo.domain.EventoSismico;

import java.util.List;

public interface IIntermediarioBDREventoSismico {
    EventoSismico findById(int id);
    List<EventoSismico> findAll();

    // Métodos de búsqueda común
    // List<EventoSismico> findAllByEstadoId(int estadoId);
    // List<EventoSismico> findAllByAnalistaId(int analistaId);

    void insert(EventoSismico evento);
    void update(EventoSismico evento);
}