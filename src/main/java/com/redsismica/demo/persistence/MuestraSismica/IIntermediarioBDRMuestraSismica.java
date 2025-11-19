package com.redsismica.demo.persistence.MuestraSismica;

import com.redsismica.demo.domain.MuestraSismica;
import com.redsismica.demo.domain.DetalleMuestraSismica;

import java.util.List;

public interface IIntermediarioBDRMuestraSismica {

    MuestraSismica findById(int id);

    List<MuestraSismica> findAll();

    List<MuestraSismica> findAllBySerieId(int idSerie);

    void insert(MuestraSismica muestra, int idSerie);

    void insertDetalle(DetalleMuestraSismica detalle, int idMuestra);

    // Si se desea, se puede agregar update() y delete()
}
