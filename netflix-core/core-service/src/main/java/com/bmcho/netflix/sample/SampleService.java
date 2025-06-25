package com.bmcho.netflix.sample;


import com.bmcho.netfilx.sample.SamplePersistencePort;
import com.bmcho.netflix.sample.response.SampleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SampleService implements SearchSampleUseCase {

//    private final SamplePersistencePort samplePersistencePort;

    @Override
    public SampleResponse getSample() {
//        String sampleName = samplePersistencePort.getSampleName("1");
//        return new SampleResponse(sampleName);
        return null;
    }
}
