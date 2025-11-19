package com.redsismica.demo.persistence.DetalleMuestraSismica;//package com.redsismica.demo.persistence.MuestraSismica;

import com.redsismica.demo.domain.DetalleMuestraSismica;
import java.util.List;

public interface IIntermediarioBDRDetalleMuestraSismica {

    DetalleMuestraSismica findById(int id);

    List<DetalleMuestraSismica> findAll();

    List<DetalleMuestraSismica> findAllByMuestraId(int idMuestra);

    void insert(DetalleMuestraSismica detalle, int idMuestra);

    // Opcional: update y delete si se requiere
}
