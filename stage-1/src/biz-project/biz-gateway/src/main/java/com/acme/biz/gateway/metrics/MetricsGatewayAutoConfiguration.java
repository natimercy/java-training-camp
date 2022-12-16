package com.acme.biz.gateway.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.config.conditional.ConditionalOnEnabledFilter;
import org.springframework.context.annotation.Bean;

/**
 * MetricsGatewayAutoConfiguration
 *
 * @author qian.he
 * @since 2022-12-13
 * @version 1.0.0
 */
@EnableConfigurationProperties
@ConditionalOnClass(MeterRegistry.class)
public class MetricsGatewayAutoConfiguration {

    @Bean
    @ConditionalOnEnabledFilter
    public HttpRequestCounterGatewayFilterFactory httpRequestCounterGatewayFilterFactory() {
        return new HttpRequestCounterGatewayFilterFactory();
    }

}
