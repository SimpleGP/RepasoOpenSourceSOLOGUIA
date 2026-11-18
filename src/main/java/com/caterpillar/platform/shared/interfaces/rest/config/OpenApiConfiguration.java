package com.caterpillar.platform.shared.interfaces.rest.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

/**
 * Describes the REST API in the generated OpenAPI document.
 */
@Configuration
@OpenAPIDefinition(info = @Info(
        title = "Caterpillar Mining Equipment Units API",
        version = "1.0.0",
        description = "API for registering mining equipment units and their operating locations."))
public class OpenApiConfiguration {
}
