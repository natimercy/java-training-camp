package com.acme.middleware.zookeeper.annotation;

import com.acme.middleware.zookeeper.core.env.ZookeeperPropertySource;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryForever;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;
import java.util.Map;

/**
 * ZookeeperConfiguration
 *
 * @author qian.he
 * @since 2023-03-29
 * @version 1.0.0
 */
public class ZookeeperConfiguration implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private MutablePropertySources propertySources;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(EnableZookeeperConfig.class.getName());
        String connectString = (String) annotationAttributes.get("connectString");
        String rootPath = (String) annotationAttributes.get("rootPath");
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(connectString)
                .retryPolicy(new RetryForever(300))
                .build();

        client.start();

        try {
            if (client.checkExists().forPath(rootPath) == null) {
                client.create().forPath(rootPath);
            }

            List<String> configBasePaths = client.getChildren().forPath(rootPath);
            for (String configBasePath : configBasePaths) {
                configBasePath = rootPath + "/" + configBasePath;
                ZookeeperPropertySource propertySource = new ZookeeperPropertySource(configBasePath, client);
                propertySources.addLast(propertySource);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.propertySources = ((ConfigurableEnvironment) environment).getPropertySources();
    }
}
