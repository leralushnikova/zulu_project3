package com.kupreychik.consts;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RegexConsts {
    public static final String PHONE_REGEX = "89\\d{9}";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String SIGNWEB_FORMAT = "\\d+";
    public static final int MIN_STUDENTS = 5;
    public static final int MAX_STUDENTS = 30;
}