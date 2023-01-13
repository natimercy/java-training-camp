package com.acme.biz.web.byteBuddy;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.Pipe;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Function;

/**
 * LoggingBeanInterceptor
 *
 * @author qian.he
 * @since 2023-01-13
 * @version 1.0.0
 */
public class LoggingBeanInterceptor {

    private final static Logger LOGGER = LoggerFactory.getLogger(LoggingBeanInterceptor.class);

    private final Object bean;

    public LoggingBeanInterceptor(Object bean) {
        this.bean = bean;
    }

    @RuntimeType
    public Object log(@Pipe Function<Object, Object> pipe, @AllArguments Object[] args, @Origin Method method) {
        Arrays.stream(args).forEach(arg -> LOGGER.info("{} 调用参数: {}", method, arg));
        try {
            return pipe.apply(bean);
        } finally {

        }
    }

}
