package com.acme.distributed.config.server;

import com.acme.distributed.config.common.ConfigEntry;
import com.acme.distributed.config.common.ConfigManager;
import com.acme.distributed.config.common.ConfigService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 * ConfigImpl
 *
 * @author natimercy
 * @version 1.0.0
 * @since 2023-05-18
 */
@Service
public class ConfigImpl implements ConfigManager, ConfigService {

    private final Map<String, ConfigEntry> cacheConfigEntrys = new HashMap<>();

    @Override
    public boolean saveConfig(ConfigEntry configEntry) {
        ConfigEntry oldConfigEntry = cacheConfigEntrys.get(configEntry.getDataId());
        if (oldConfigEntry != null) {
            String content = configEntry.getContent();
            byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
            long checksum = getChecksum(DigestUtils.md5DigestAsHex(bytes));
            if (oldConfigEntry.getChecksum() != checksum) {
                configEntry.setUpdateTime(LocalDateTime.now());
                configEntry.setChecksum(checksum);
                cacheConfigEntrys.put(configEntry.getDataId(), configEntry);
            }
        } else {
            configEntry.setCreateTime(LocalDateTime.now());
            configEntry.setUpdateTime(LocalDateTime.now());
            String content = configEntry.getContent();
            byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
            configEntry.setChecksum(getChecksum(DigestUtils.md5DigestAsHex(bytes)));
            cacheConfigEntrys.put(configEntry.getDataId(), configEntry);
        }

        return true;
    }

    @Override
    public ConfigEntry getConfig(String dataId) {
        return cacheConfigEntrys.get(dataId);
    }

    @Override
    public boolean deleteConfig(String dataId) {
        ConfigEntry configEntry = cacheConfigEntrys.remove(dataId);
        return Objects.nonNull(configEntry);
    }

    public static long getChecksum(String content) {
        Checksum checksum = new CRC32();
        byte[] bytes = content.getBytes();
        checksum.update(bytes, 0, bytes.length);
        return checksum.getValue();
    }

}
