package com.acme.middleware.zookeeper.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * EnableZookeeperConfig
 *
 * @author qian.he
 * @since 2023-04-04
 * @version 1.0.0
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ZookeeperConfiguration.class)
public @interface EnableZookeeperConfig {

    String connectString() default "127.0.0.1:2181";

    String rootPath() default "/configs";

}
