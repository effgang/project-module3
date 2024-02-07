package com.efanov.constant;


import lombok.experimental.UtilityClass;


@UtilityClass
public class RegexConstant {

    public static final String PHONE_REGEX = "^\\+?7(\\d{10})$";

    public static final String DATE_FORMAT = "^([0-2][0-9]||3[0-1]).(0[0-9]||1[0-2]).([0-9][0-9])?[0-9][0-9]$";
}
