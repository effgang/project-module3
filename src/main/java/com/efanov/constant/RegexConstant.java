package com.efanov.constant;


import lombok.experimental.UtilityClass;


@UtilityClass
public class RegexConstant {

    public static final String PHONE_REGEX = "^\\+?7(\\d{10})$";

    public static final String DATE_FORMAT = "^([0-2][0-9]||3[0-1]).(0[0-9]||1[0-2]).([0-9][0-9])?[0-9][0-9]$";
    public static final String DATE = "^([0]\\d|[1][0-2])-([0-2]\\d|[3][0-1])-([2][01]|[1][6-9])\\d{2}(\\s([0-1]\\d|[2][0-3])(\\:[0-5]\\d){1,2})?$";
}
