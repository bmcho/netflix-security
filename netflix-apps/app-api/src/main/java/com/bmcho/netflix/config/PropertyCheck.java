package com.bmcho.netflix.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class PropertyCheck {

    private final Environment env;

    @PostConstruct
    public void checkDatasourceProperties() {
        String url = env.getProperty("spring.datasource.url");
        log.info("spring datasource url : {}", url);
    }

}
