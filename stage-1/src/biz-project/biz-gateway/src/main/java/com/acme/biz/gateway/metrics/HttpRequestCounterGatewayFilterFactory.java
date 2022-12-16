package com.acme.biz.gateway.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;

/**
 * HttpRequestCounterGatewayFilterFactory
 *
 * @author qian.he
 * @since 2022-12-13
 * @version 1.0.0
 */
public class HttpRequestCounterGatewayFilterFactory
        extends AbstractGatewayFilterFactory<HttpRequestCounterGatewayFilter.HttpRequestCounterConfig>
        implements MeterBinder {

    private MeterRegistry registry;

    public HttpRequestCounterGatewayFilterFactory() {
        super(HttpRequestCounterGatewayFilter.HttpRequestCounterConfig.class);
    }

    @Override
    public GatewayFilter apply(HttpRequestCounterGatewayFilter.HttpRequestCounterConfig config) {
        return new HttpRequestCounterGatewayFilter(config, registry);
    }

    @Override
    public void bindTo(MeterRegistry registry) {
        this.registry = registry;
    }

}
