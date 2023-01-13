package com.acme.biz.web.bean;

import com.acme.biz.api.interfaces.UserRegistrationService;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * ByteBuddyLoggingBeanPostProcessor
 *
 * @author qian.he
 * @since 2023-01-13
 * @version 1.0.0
 */
@Component
public class ByteBuddyLoggingBeanPostProcessor implements BeanFactoryPostProcessor,
        SmartInstantiationAwareBeanPostProcessor,
        MergedBeanDefinitionPostProcessor,
        BeanClassLoaderAware {

    private ClassLoader classLoader;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
        String beanClassName = beanDefinition.getBeanClassName();
        Class<?> beanClass = ClassUtils.resolveClassName(beanClassName, classLoader);

        Class<?> newProxyClass = newProxyClass(beanClass, beanName);
        beanDefinition.setBeanClass(newProxyClass);
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return SmartInstantiationAwareBeanPostProcessor.super.postProcessBeforeInstantiation(beanClass, beanName);
    }

    @Override
    public Class<?> predictBeanType(Class<?> beanClass, String beanName) throws BeansException {
        if (UserRegistrationService.class.isAssignableFrom(beanClass)) {
            return newProxyClass(beanClass, beanName);
        }

        return SmartInstantiationAwareBeanPostProcessor.super.predictBeanType(beanClass, beanName);
    }

    private Class<?> newProxyClass(Class<?> beanType, String beanName) {
        Class<?> dynamicProxyClass = null;
        try {
            dynamicProxyClass = new ByteBuddy()
                    .subclass(beanType)
                    .method(named("toString"))
                    .intercept(FixedValue.value("UserRegistrationService"))
                    .make()
                    .load(classLoader)
                    .getLoaded();
        } catch (Exception e) {
            throw new BeanCreationException("Bean[name : " + beanName + "] creation is failed", e);
        }
        return dynamicProxyClass;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

}
