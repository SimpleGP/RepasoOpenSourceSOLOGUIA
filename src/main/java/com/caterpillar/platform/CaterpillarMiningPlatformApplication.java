package com.caterpillar.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Starts the Caterpillar Mining Platform API and enables persistence auditing.
 */
@EnableJpaAuditing
@SpringBootApplication
public class CaterpillarMiningPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(CaterpillarMiningPlatformApplication.class, args);
    }
}
