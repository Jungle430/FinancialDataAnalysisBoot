package com.bupt.Jungle.FinancialDataAnalysis.infrastructure.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HttpGateway {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
