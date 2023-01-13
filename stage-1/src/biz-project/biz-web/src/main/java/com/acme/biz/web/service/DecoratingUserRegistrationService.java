package com.acme.biz.web.service;

import com.acme.biz.api.exception.UserException;
import com.acme.biz.api.interfaces.UserRegistrationService;
import com.acme.biz.api.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DecoratingUserRegistrationService
 *
 * @author qian.he
 * @since 2023-01-13
 * @version 1.0.0
 */
public class DecoratingUserRegistrationService implements UserRegistrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DecoratingUserRegistrationService.class);

    private final UserRegistrationService delegate;

    public DecoratingUserRegistrationService(UserRegistrationService delegate) {
        this.delegate = delegate;
    }

    @Override
    public Boolean registerUser(User user) throws UserException {
        LOGGER.info("registerUser information.........");
        return delegate.registerUser(user);
    }
}
