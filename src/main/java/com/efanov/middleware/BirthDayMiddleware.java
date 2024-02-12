package com.efanov.middleware;

import com.efanov.dto.AbstractPeople;
import lombok.extern.slf4j.Slf4j;

import static com.efanov.constant.LogConstant.*;
import static com.efanov.constant.RegexConstant.DATE_FORMAT;

@Slf4j
public class BirthDayMiddleware extends Middleware {

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
