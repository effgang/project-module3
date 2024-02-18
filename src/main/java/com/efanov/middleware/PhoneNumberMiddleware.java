package com.efanov.middleware;

import com.efanov.constant.LogConstant;
import com.efanov.dto.AbstractPeople;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.efanov.constant.LogConstant.CHECKING_PHONE;
import static com.efanov.constant.RegexConstant.PHONE_REGEX;


public class PhoneNumberMiddleware extends Middleware {
    private static final Logger log = LoggerFactory.getLogger(PhoneNumberMiddleware.class.getName());

    public boolean check(AbstractPeople model) {
        log.debug(CHECKING_PHONE);
        if (model.getPhoneNumber().matches(PHONE_REGEX)) {
            log.debug(LogConstant.VALID_PHONE);
            return checkNext(model);
        } else {
            log.debug(LogConstant.INVALID_PHONE);
            return false;
        }
    }
}