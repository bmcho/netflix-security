package com.bmcho.netflix.respository.sample;


import com.bmcho.netflix.entity.SampleEntity;
import com.bmcho.netfilx.sample.SamplePersistencePort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SampleRepository implements SamplePersistencePort {

    private final SampleJpaRepository sampleJpaRepository;

    @Override
    @Transactional
    public String getSampleName(String id) {
        Optional<SampleEntity> sampleEntity = sampleJpaRepository.findById(id);
        return sampleEntity.map(SampleEntity::getSampleName).orElse("Test Name");
    }
}
