package ru.kalmykov.whatsappsender.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.annotation.ParametersAreNonnullByDefault;

@Configuration
@ParametersAreNonnullByDefault
public class RestConfig {

    @Bean
    public RestTemplate rest1CTemplate() {
        return new RestTemplate();
    }
}
