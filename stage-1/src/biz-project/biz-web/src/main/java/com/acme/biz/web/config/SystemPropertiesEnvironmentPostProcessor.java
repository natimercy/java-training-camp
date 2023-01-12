package com.acme.biz.web.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;

import java.util.Map;

/**
 * SystemPropertiesEnvironmentPostProcessor
 *
 * @author qian.he
 * @since 2022-12-19
 * @version 1.0.0
 */
public class SystemPropertiesEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String sourceName = StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME;
        MutablePropertySources propertySources = environment.getPropertySources();
        Map<String, Object> systemEnvironment = environment.getSystemEnvironment();

        propertySources.replace(sourceName, new MapPropertySource(sourceName, systemEnvironment));


    }
}
