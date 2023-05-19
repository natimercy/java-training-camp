package com.acme.distributed.config.client;

/**
 * todo
 *
 * @author natimercy
 * @version 1.0.0
 * @since 2023-05-19
 */
public class ConfigClientConfiguration {

    private String dataId;

    private String serverAddr;

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getServerAddr() {
        return serverAddr;
    }

    public void setServerAddr(String serverAddr) {
        this.serverAddr = serverAddr;
    }

    @Override
    public String toString() {
        return "ConfigClientConfiguration{" + "dataId='" + dataId + '\'' + ", serverAddr='" + serverAddr + '\'' + '}';
    }

    public static class ConfigClientConfigurationBuilder {

        public ConfigClientConfigurationBuilder() {
        }

        private String dataId;

        private String serverAddr;

        public ConfigClientConfigurationBuilder dataId(String dataId) {
            this.dataId = dataId;
            return this;
        }

        public ConfigClientConfigurationBuilder serverAddr(String serverAddr) {
            this.serverAddr = serverAddr;
            return this;
        }

        public ConfigClientConfiguration builder() {
            ConfigClientConfiguration configuration = new ConfigClientConfiguration();
            configuration.setDataId(this.dataId);
            configuration.setServerAddr(this.serverAddr);
            return configuration;
        }
    }

}
