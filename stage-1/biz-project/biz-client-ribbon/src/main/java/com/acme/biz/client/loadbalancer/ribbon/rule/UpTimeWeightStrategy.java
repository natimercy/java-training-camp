package com.acme.biz.client.loadbalancer.ribbon.rule;

import java.time.Duration;

/**
 * TODO
 *
 * @author natimercy
 * @date 2022-11-17
 * @since 1.0.0
 */
public interface UpTimeWeightStrategy {

    double getWeight(Duration duration);

    double NORMAL_WEIGHT = 100.0d;

}
