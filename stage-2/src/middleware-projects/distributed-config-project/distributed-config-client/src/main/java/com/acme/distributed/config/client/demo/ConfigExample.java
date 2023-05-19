package com.acme.distributed.config.client.demo;

import com.acme.distributed.config.client.ConfigClient;
import com.acme.distributed.config.client.ConfigClientConfiguration;
import com.acme.distributed.config.common.ConfigEntry;

/**
 * todo
 *
 * @author natimercy
 * @version 1.0.0
 * @since 2023-05-19
 */
public class ConfigExample {

    public static void main(String[] args) {
        ConfigClientConfiguration configuration = new ConfigClientConfiguration.ConfigClientConfigurationBuilder()
                .dataId("test.txt")
                .serverAddr("127.0.0.1:5050")
                .builder();
        ConfigClient configClient = new ConfigClient(configuration);
        ConfigEntry config = configClient.getConfig();
        System.out.println(config);
    }

}
