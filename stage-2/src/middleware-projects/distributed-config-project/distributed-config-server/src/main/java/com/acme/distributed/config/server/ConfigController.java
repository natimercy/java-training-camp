package com.acme.distributed.config.server;

import com.acme.distributed.config.common.ApiResponse;
import com.acme.distributed.config.common.ConfigEntry;
import com.acme.distributed.config.common.ConfigManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * todo
 *
 * @author natimercy
 * @version 1.0.0
 * @since 2023-05-19
 */
@RestController
@RequestMapping("/config")
public class ConfigController {

    private final ConfigManager configManager;

    public ConfigController(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @PostMapping("/saveConfig")
    public ApiResponse<Boolean> saveConfig(@RequestBody ConfigEntry configEntry) {
        return ApiResponse.ok(configManager.saveConfig(configEntry));
    }

    @GetMapping("/getConfig")
    public ApiResponse<ConfigEntry> getConfig(@RequestParam(value = "dataId") String dataId) {
        return ApiResponse.ok(configManager.getConfig(dataId));
    }

    @PostMapping("/deleteConfig")
    public ApiResponse<Boolean> deleteConfig(@RequestBody String dataId) {
        return ApiResponse.ok(configManager.deleteConfig(dataId));
    }

}
