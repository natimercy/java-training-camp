package com.acme.middleware.zookeeper.core.env;

import com.acme.middleware.zookeeper.ConfigEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.core.env.EnumerablePropertySource;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * ZookeeperPropertySource
 *
 * @author qian.he
 * @since 2023-03-29
 * @version 1.0.0
 */
public class ZookeeperPropertySource extends EnumerablePropertySource<Map<String, Object>> {

    private String configBasePath;

    private CuratorFramework client;

    private Map<String, Object> cache;

    public ZookeeperPropertySource(String configBasePath, CuratorFramework client) {
        super("zookeeper://" + configBasePath);
        this.configBasePath = configBasePath;
        this.client = client;
        initCache();
    }

    private void initCache() {
        Map<String, Object> localCache = new HashMap<>();
        try {
            List<String> propertyNamePaths = client.getChildren().forPath(configBasePath);
            for (String propertyNamePath : propertyNamePaths) {
                ConfigEntity configEntity = getConfigEntity(configBasePath + "/" + propertyNamePath, client);
                String contentType = configEntity.getHeader().getContentType();
                String body = configEntity.getBody();
                switch (contentType) {
                    case "text/plain":
                        localCache.put(propertyNamePath, body);
                        break;
                    case "text/properties":
                        Properties properties = new Properties();
                        try {
                            properties.load(new StringReader(body));
                            localCache.putAll((Map) properties);
                        } catch (IOException e) {
                            // TODO
                        }
                        break;
                    case "text/json":
                        // TODO
                        break;
                    case "text/xml":
                        // TODO
                        break;
                    default:
                        System.out.println("===============");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        this.cache = localCache;

        // 注册一个 watcher 循环监视，监听 zookeeper 配合文件（包含数据）是否发生变化
    }

    @Override
    public String[] getPropertyNames() {
        return this.cache.keySet().toArray(new String[0]);
    }

    @Override
    public Object getProperty(String name) {
        return this.cache.get(name);
    }

    private ConfigEntity getConfigEntity(String path, CuratorFramework client) throws Exception {
        byte[] bytes = client.getData().forPath(path);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(bytes, ConfigEntity.class);
    }
}
