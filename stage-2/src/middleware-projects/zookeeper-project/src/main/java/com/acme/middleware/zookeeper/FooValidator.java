package com.acme.middleware.zookeeper;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * todo
 *
 * @author qian.he
 * @since 2023-04-12
 * @version 1.0.0
 */
public class FooValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(Foo.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }

    class Foo {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
