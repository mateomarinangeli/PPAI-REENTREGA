package com.redsismica.demo.persistence.MagnitudRichter;

import com.redsismica.demo.domain.MagnitudRichter;

import java.util.List;

public interface IIntermediarioBDRMagnitudRichter {
    MagnitudRichter findById(int id);
    List<MagnitudRichter> findAll();
    void insert(MagnitudRichter magnitud);
    void update(MagnitudRichter magnitud);
}