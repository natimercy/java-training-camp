package com.acme.distributed.config.common;

/**
 * todo
 *
 * @author natimercy
 * @version 1.0.0
 * @since 2023-05-18
 */
public interface ConfigManager {

    boolean saveConfig(ConfigEntry configEntry);

    ConfigEntry getConfig(String dataId);

    boolean deleteConfig(String dataId);

}
