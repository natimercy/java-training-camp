package com.acme.biz.web.bean;

import com.acme.biz.api.interfaces.UserRegistrationService;
import com.acme.biz.web.service.DecoratingUserRegistrationService;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * todo
 *
 * @author qian.he
 * @since 2023-01-13
 * @version 1.0.0
 */
@Component
public class DecoratingUserRegistrationServiceBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = AopUtils.getTargetClass(bean);
        if (UserRegistrationService.class.isAssignableFrom(beanClass)) {
            UserRegistrationService userRegistrationService = (UserRegistrationService) bean;
            return new DecoratingUserRegistrationService(userRegistrationService);
        }

        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
