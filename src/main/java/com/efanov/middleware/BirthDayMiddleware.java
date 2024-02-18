package com.efanov.middleware;

import com.efanov.dto.AbstractPeople;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.efanov.constant.LogConstant.*;
import static com.efanov.constant.RegexConstant.DATE_FORMAT;


public class BirthDayMiddleware extends Middleware {
    private static final Logger log = LoggerFactory.getLogger(BirthDayMiddleware.class.getName());

    @Override
    public boolean check(AbstractPeople model) {
        log.debug(CHECKING_BIRTH_DATE);
        if (model.getBirthday().matches(DATE_FORMAT)) {
            log.debug(VALID_DATE_FORMAT);
            return checkNext(model);
        } else {
            log.debug(INVALID_FATE_FORMAT);
            return false;
        }

    }
}
