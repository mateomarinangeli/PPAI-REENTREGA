package com.redsismica.demo.persistence.AlcanceSismo;

import com.redsismica.demo.domain.AlcanceSismo;

import java.util.List;

public interface IIntermediarioBDRAlcanceSismo {
    AlcanceSismo findById(int id);
    List<AlcanceSismo> findAll();
    void insert(AlcanceSismo alcance);
    void update(AlcanceSismo alcance);
}