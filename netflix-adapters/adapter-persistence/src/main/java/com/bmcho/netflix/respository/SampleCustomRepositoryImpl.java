package com.bmcho.netflix.respository;

import com.bmcho.netflix.entity.QSampleEntity;
import com.bmcho.netflix.entity.SampleEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SampleCustomRepositoryImpl implements SampleCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<SampleEntity> findAllByQueryDsl() {
        return jpaQueryFactory.selectFrom(QSampleEntity.sampleEntity).fetch();
    }
}
