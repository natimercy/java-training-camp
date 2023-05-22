package com.acme.distributed.config.client;

import com.acme.distributed.config.common.ApiResponse;
import com.acme.distributed.config.common.ConfigEntry;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static com.acme.distributed.config.client.Constants.DATA_ID;

/**
 * todo
 *
 * @author natimercy
 * @version 1.0.0
 * @since 2023-05-19
 */
public class ConfigClient {

    private final ConfigClientConfiguration configuration;

    private final RestTemplate restTemplate = new RestTemplate();

    public ConfigClient(ConfigClientConfiguration configuration) {
        this.configuration = configuration;
    }

    public ConfigEntry getConfig() {
        String dataId = configuration.getDataId();
        String serverAddr = configuration.getServerAddr();
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put(DATA_ID, dataId);
        String url = "http://" + serverAddr + Constants.GET_CONFIG_PATH + "/?dataId={dataId}";

        ParameterizedTypeReference<ApiResponse<ConfigEntry>> typeReference = new ParameterizedTypeReference<ApiResponse<ConfigEntry>>() {
        };
        ResponseEntity<ApiResponse<ConfigEntry>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null,
                typeReference, uriVariables);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            ApiResponse<ConfigEntry> response = responseEntity.getBody();
            assert response != null;
            return response.getBody();
        }

        return null;
    }
}
