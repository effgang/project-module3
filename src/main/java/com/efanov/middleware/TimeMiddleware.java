package com.efanov.middleware;

import com.efanov.dto.timetable.TimetableRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.efanov.constant.LogConstant.*;
import static com.efanov.constant.RegexConstant.DATE;


public class TimeMiddleware {
    private static final Logger log = LoggerFactory.getLogger(TimeMiddleware.class.getName());

    public boolean check(TimetableRequest timetable) {
        log.debug(CHECKING_TIME);
        if (timetable.getTimeStart().matches(DATE) && timetable.getTimeEnd().matches(DATE)) {
            log.debug(VALID_TIME);
            return false;
        } else {
            log.debug(INVALID_TIME);
            return true;
        }
    }
}
