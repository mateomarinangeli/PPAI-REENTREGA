package com.redsismica.demo.persistence.TipoDeDato;

import com.redsismica.demo.domain.TipoDeDato;
import java.util.List;

public interface IIntermediarioBDRTipoDeDato {

    TipoDeDato findById(int id);

    List<TipoDeDato> findAll();

    void insert(TipoDeDato tipo);

    // Opcional: update y delete si se requiere
}
