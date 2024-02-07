package com.efanov.middleware;

import com.efanov.constant.LogConstant;
import com.efanov.dto.student.AbstractPeople;
import lombok.extern.slf4j.Slf4j;

import static com.efanov.constant.LogConstant.CHECKING_PHONE;
import static com.efanov.constant.RegexConstant.PHONE_REGEX;

@Slf4j
public class PhoneNumberMiddleware extends Middleware {

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