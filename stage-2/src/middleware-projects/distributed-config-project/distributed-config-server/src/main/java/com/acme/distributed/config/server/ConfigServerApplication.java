package com.acme.distributed.config.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ConfigServerApplication
 *
 * @author natimercy
 * @version 1.0.0
 * @since 2023-05-19
 */
@SpringBootApplication
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }

}
