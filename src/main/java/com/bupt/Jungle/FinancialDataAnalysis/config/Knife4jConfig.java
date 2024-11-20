package com.bupt.Jungle.FinancialDataAnalysis.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {
    private final String title;

    private final String version;

    private final String description;

    private final String license;

    private final String name;

    @Autowired
    public Knife4jConfig(
            @Value("${personal.knife4j.title}") String title,
            @Value("${personal.knife4j.version}") String version,
            @Value("${personal.knife4j.description}") String description,
            @Value("${personal.knife4j.license}") String license,
            @Value("${personal.knife4j.name}") String name
    ) {
        this.title = title;
        this.version = version;
        this.description = description;
        this.license = license;
        this.name = name;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(
                new Info()
                        .title(title)
                        .version(version)
                        .description(description)
                        .contact(new Contact().name(name))
                        .license(new License().name(license))
        );
    }
}
