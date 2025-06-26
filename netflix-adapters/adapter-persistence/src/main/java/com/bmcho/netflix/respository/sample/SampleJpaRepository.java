package com.bmcho.netflix.respository.sample;


import com.bmcho.netflix.entity.SampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleJpaRepository extends JpaRepository<SampleEntity, String>, SampleCustomRepository {
}
