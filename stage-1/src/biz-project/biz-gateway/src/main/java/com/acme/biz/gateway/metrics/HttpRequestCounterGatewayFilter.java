package com.acme.biz.gateway.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpMethod;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * HttpRequestCounterGatewayFilter
 *
 * @author qian.he
 * @since 2022-12-13
 * @version 1.0.0
 */
public class HttpRequestCounterGatewayFilter implements GatewayFilter {

    private final HttpRequestCounterConfig counterConfig;

    private final MeterRegistry registry;

    public HttpRequestCounterGatewayFilter(HttpRequestCounterConfig counterConfig, MeterRegistry registry) {
        this.counterConfig = counterConfig;
        this.registry = registry;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String name = exchange.getRequest().getMethod().name();
        if (!counterConfig.isIgnoreMethod(name)) {
            Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
            String id = route.getId();
            Counter counter = Counter.builder(String.format("COUNTER.gateway.%s", id))
                    .tags("instanceId", this.counterConfig.instanceTag)
                    .register(registry);

            counter.increment();
        }

        return chain.filter(exchange);
    }


    public static class HttpRequestCounterConfig {

        private String instanceTag;

        private List<HttpMethod> ignoredMethod = new ArrayList<>();

        public String getInstanceTag() {
            return instanceTag;
        }

        public void setInstanceTag(String instanceTag) {
            this.instanceTag = instanceTag;
        }

        public List<HttpMethod> getIgnoredMethod() {
            return ignoredMethod;
        }

        public void setIgnoredMethod(List<HttpMethod> ignoredMethod) {
            this.ignoredMethod = ignoredMethod;
        }

        public boolean isIgnoreMethod(String name) {
            for (HttpMethod httpMethod : ignoredMethod) {
                return httpMethod.matches(name);
            }
            return false;
        }

        @Override
        public String toString() {
            return "HttpRequestCounterConfig{" +
                    "instanceTag='" + instanceTag + '\'' +
                    ", ignoredMethod=" + ignoredMethod +
                    '}';
        }
    }
}
