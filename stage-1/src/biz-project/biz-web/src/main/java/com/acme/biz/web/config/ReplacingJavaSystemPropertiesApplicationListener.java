package com.acme.biz.web.config;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.env.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.core.env.StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME;

/**
 * ReplacingJavaSystemPropertiesApplicationListener
 *
 * @author qian.he
 * @since 2023-01-12
 * @version 1.0.0
 */
public class ReplacingJavaSystemPropertiesApplicationListener
        implements ApplicationListener<ApplicationEnvironmentPreparedEvent>, Ordered {

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment environment = event.getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        String propertySourceName = SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME;
        PropertySource<?> propertySource = propertySources.get(propertySourceName);
        if (propertySource instanceof PropertiesPropertySource) {
            Map properties = new HashMap<>(System.getProperties());
            propertySources.replace(propertySourceName, new MapPropertySource(propertySourceName, properties));
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
