package com.acme.middleware.zookeeper;

import com.acme.middleware.zookeeper.config.AppConfig;
import com.acme.middleware.zookeeper.config.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cglib.core.DebuggingClassWriter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * todo
 *
 * @author qian.he
 * @since 2023-03-30
 * @version 1.0.0
 */
@SpringBootApplication
@RestController
@EnableTransactionManagement
public class ZookeeperConfigApplication {

    public static void main(String[] args) {
        // 保存生成的class的路径，对应的代码在DebuggingClassWriter#DEBUG_LOCATION_PROPERTY字段，toByteArray方法
       /* System.getProperties().setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY,
                "E:\\code\\java-training-camp\\stage-2\\class");*/


        ConfigurableApplicationContext run = SpringApplication.run(ZookeeperConfigApplication.class, args);
        AppConfig bean = run.getBean(AppConfig.class);
        System.out.println(bean);
        Map<String, ClientService> beansOfType = run.getBeansOfType(ClientService.class);
        beansOfType.forEach((beanName, clientService) -> {
            System.out.println("beanName: " + beanName + " , clientService" + clientService);
        });


        Map<String, ClientService> beansOfType1 = run.getBeansOfType(ClientService.class);
        beansOfType1.forEach((beanName, clientService) -> {
            System.out.println("beanName: " + beanName + " , clientService" + clientService);
        });

        System.setProperty("cglib.debugLocation", "E:\\code\\java-training-camp\\stage-2");
    }

    @Autowired
    Environment environment;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(new FooValidator());
    }

    @Transactional(rollbackFor = Exception.class)
    @GetMapping(value = "message1")
    public String message(String message) {
        return message;
    }

}
