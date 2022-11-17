package com.acme.biz.client.loadbalancer.ribbon.rule;

import com.netflix.appinfo.LeaseInfo;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import org.springframework.util.CollectionUtils;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author natimercy
 * @date 2022-11-17
 * @since 1.0.0
 */
public class WeightedUpTimeRule extends AbstractLoadBalancerRule {

    private final UpTimeWeightStrategy strategy;

    private SecureRandom random = new SecureRandom();

    public WeightedUpTimeRule() {
        this(new DefaultUpTimeWeightStrategy());
    }

    public WeightedUpTimeRule(UpTimeWeightStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public Server choose(Object key) {
        if (getLoadBalancer() == null) {
            return null;
        }

        long current = System.currentTimeMillis() / 1000;
        List<Server> accessibleServer = getLoadBalancer().getReachableServers();
        if (CollectionUtils.isEmpty(accessibleServer)) {
            return null;
        }
        List<ServerWeightHolder> serverWeightHolders = accessibleServer.stream()
                .map(server -> getServerWeightHolder(current, server)).sorted()
                .collect(Collectors.toList());

        boolean isSameWeight = true;
        double totalWeight = serverWeightHolders.stream()
                .mapToDouble(ServerWeightHolder::getWeight)
                .sum();

        for (int i = 0; i < serverWeightHolders.size(); i++) {
            double weight = serverWeightHolders.get(i).getWeight();
            totalWeight += weight;

            if (isSameWeight && totalWeight != weight * (i + 1)) {
                isSameWeight = false;
            }
        }

        if (isSameWeight) {
            int randomIndex = random.nextInt() * accessibleServer.size();
            return accessibleServer.get(randomIndex);
        }

        double maxWeight = serverWeightHolders.get(serverWeightHolders.size() - 1).getWeight();
        double randomWeight = random.nextDouble() * maxWeight;
        // pick the server index based on the randomIndex
        for (ServerWeightHolder holder : serverWeightHolders) {
            final double weight = holder.getWeight();
            if (weight >= randomWeight) {
                return holder.getServer();
            }
        }

        throw new RuntimeException("no service found");
    }

    private ServerWeightHolder getServerWeightHolder(long current, Server server) {
        Duration duration = getDuration(server, current);
        double weight = this.strategy.getWeight(duration);
        return new ServerWeightHolder(server, weight);
    }

    private Duration getDuration(Server server, long currentTimeSeconds) {
        if (server instanceof DiscoveryEnabledServer) {
            DiscoveryEnabledServer discoveryEnabledServer = (DiscoveryEnabledServer) server;
            long uptime = getUpTimeMills(discoveryEnabledServer);
            // 转到秒
            uptime = uptime / 1000;
            return Duration.ofSeconds(currentTimeSeconds - uptime);
        } else {
            return Duration.ZERO;
        }
    }

    protected long getUpTimeMills(DiscoveryEnabledServer server) {
        LeaseInfo leaseInfo = server.getInstanceInfo().getLeaseInfo();
        return leaseInfo.getServiceUpTimestamp();
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {

    }

    static class ServerWeightHolder implements Comparable<ServerWeightHolder> {

        private final Server server;

        private final double weight;

        public ServerWeightHolder(Server server, double weight) {
            this.server = server;
            this.weight = weight;
        }

        public double getWeight() {
            return this.weight;
        }

        public Server getServer() {
            return this.server;
        }


        @Override
        public int compareTo(ServerWeightHolder o) {
            if (o == this) {
                return 0;
            }

            return Double.compare(this.weight, o.getWeight());
        }
    }
}
