package com.redsismica.demo.persistence.SerieTemporal;

import com.redsismica.demo.domain.SerieTemporal;

import java.util.List;

public interface IIntermediarioBDRSerieTemporal {
    //SerieTemporal findById(int id);
    //List<SerieTemporal> findAll();

    // 🎯 Método clave para ensamblar EventoSismico (Datos sísmicos)
    //List<SerieTemporal> findAllByEventoId(int eventoId);

    SerieTemporal findById(int id);

    List<SerieTemporal> findAllByEventoId(int eventoId);

    void insert(SerieTemporal serie, int idEvento);
}