package com.bmcho.netflix.respository.sample;

import com.bmcho.netflix.entity.SampleEntity;

import java.util.List;

public interface SampleCustomRepository {
    List<SampleEntity> findAllByQueryDsl();
}
