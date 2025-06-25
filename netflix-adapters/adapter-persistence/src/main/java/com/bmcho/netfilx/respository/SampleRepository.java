package com.bmcho.netfilx.respository;


import com.bmcho.netfilx.sample.SamplePersistencePort;
import lombok.RequiredArgsConstructor;

//@Repository
@RequiredArgsConstructor
public class SampleRepository implements SamplePersistencePort {

    private final SampleJpaRepository sampleJpaRepository;

    @Override
//    @Transactional
    public String getSampleName(String id) {
        return "";
//        Optional<SampleEntity> sampleEntity = sampleJpaRepository.findById(id);
//        return sampleEntity.map(SampleEntity::getSampleName).orElseThrow();
    }
}
