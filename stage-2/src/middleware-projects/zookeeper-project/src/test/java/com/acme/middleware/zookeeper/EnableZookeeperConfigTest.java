package com.acme.middleware.zookeeper;

import com.acme.middleware.zookeeper.annotation.EnableZookeeperConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryForever;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StreamUtils;

import static org.junit.Assert.assertEquals;

/**
 * EnableZookeeperConfigTest
 *
 * @author qian.he
 * @since 2023-04-04
 * @version 1.0.0
 */
@RunWith(SpringRunner.class)
@EnableZookeeperConfig
@ContextConfiguration(classes = EnableZookeeperConfigTest.class)
public class EnableZookeeperConfigTest {

    @BeforeClass
    public static void init() throws Exception {
        EnableZookeeperConfig annotation = EnableZookeeperConfigTest.class.getDeclaredAnnotation(EnableZookeeperConfig.class);
        String connectString = annotation.connectString();
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .retryPolicy(new RetryForever(300))
                .connectString(connectString)
                .build();

        client.start();

        String rootPath = annotation.rootPath();

        markDir(client, rootPath);

        mockConfigEntity(client, rootPath, "config1");

        client.close();
    }

    private static void mockConfigEntity(CuratorFramework client, String rootPath, String configBasePath) throws Exception {
        String propertySourcePath = rootPath + "/" + configBasePath;

        markDir(client, propertySourcePath);

        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resourceResolver.getResources("classpath:/MATA-INF/zookeeper/*.json");
        for (Resource resource : resources) {
            String propertyName = resource.getFilename();
            String propertyPath = propertySourcePath + "/" + propertyName;
            if (client.checkExists().forPath(propertyPath) != null) {
                client.delete().forPath(propertyPath);
            }

            byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
            client.create().forPath(propertyPath, bytes);
        }
    }

    private static void markDir(CuratorFramework client, String path) throws Exception {
        if (client.checkExists().forPath(path) == null) {
            client.create().forPath(path);
        }
    }

    @Autowired
    private Environment environment;

    @Test
    public void test() {
        assertEquals("test-name", environment.getProperty("my.name.json"));
    }
}
