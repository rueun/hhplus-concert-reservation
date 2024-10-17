package com.hhplus.concertreservation.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.annotation.PostConstruct;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class SwaggerConfig {
    private static final String QUEUE_TOKEN_SCHEME_NAME = "queue-token";
    private static final String USER_ID_SCHEME_NAME = "user-id";
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(createComponents())
                .info(apiInfo())
                .addSecurityItem(createSecurityRequirement());
    }

    private Components createComponents() {
        SecurityScheme queueTokenScheme = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("QUEUE-TOKEN");

        SecurityScheme userIdScheme = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("USER-ID");

        return new Components()
                .addSecuritySchemes(QUEUE_TOKEN_SCHEME_NAME, queueTokenScheme)
                .addSecuritySchemes(USER_ID_SCHEME_NAME, userIdScheme);
    }

    private SecurityRequirement createSecurityRequirement() {
        return new SecurityRequirement()
                .addList(QUEUE_TOKEN_SCHEME_NAME)
                .addList(USER_ID_SCHEME_NAME);
    }

    private Info apiInfo() {
        return new Info()
                .title("Concert Reservation Service API")
                .version("1.0.0");
    }

    @PostConstruct
    public void setUp() {
        Schema<?> localDateTimeSchema = new StringSchema()
                .example(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
        SpringDocUtils.getConfig().replaceWithSchema(LocalDateTime.class, localDateTimeSchema);
    }
}
