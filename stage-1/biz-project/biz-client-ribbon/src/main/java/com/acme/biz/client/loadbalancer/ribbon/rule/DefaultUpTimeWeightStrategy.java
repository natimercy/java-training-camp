package com.acme.biz.client.loadbalancer.ribbon.rule;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

/**
 * TODO
 *
 * @author natimercy
 * @date 2022-11-17
 * @since 1.0.0
 */
public class DefaultUpTimeWeightStrategy implements UpTimeWeightStrategy {

    public final TemporalUnit MILLIS_UNIT = ChronoUnit.SECONDS;

    private final long RANGE_INIT = 30;

    private final long RANGE_NORMAL = 10 * RANGE_INIT;

    @Override
    public double getWeight(Duration duration) {
        long value = duration.get(MILLIS_UNIT);
        if (value < RANGE_INIT) {
            return 1.0;
        } else if (value < RANGE_NORMAL) {
            return 10.0;
        } else {
            return NORMAL_WEIGHT;
        }
    }
}
