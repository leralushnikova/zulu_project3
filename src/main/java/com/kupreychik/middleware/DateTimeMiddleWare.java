package com.kupreychik.middleware;

import com.kupreychik.dto.request.TimeTableRequest;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.kupreychik.consts.RegexConsts.DATETIME_FORMAT;

@Slf4j
public class DateTimeMiddleWare extends Middleware{

    @Override
    public boolean check(Object model) {
        log.debug("Выполняется проверка даты расписания");
        try {
            if (model instanceof TimeTableRequest timeTableRequest) {
                LocalDateTime.parse(timeTableRequest.getStartDateTime(), DateTimeFormatter.ofPattern(DATETIME_FORMAT));
                LocalDateTime.parse(timeTableRequest.getEndDateTime(), DateTimeFormatter.ofPattern(DATETIME_FORMAT));
            }
            if (model instanceof String s) {
                LocalDateTime.parse(s, DateTimeFormatter.ofPattern(DATETIME_FORMAT));
            }
            log.debug("Проверка пройдена. Дата подходит");
            return checkNext(model);
        } catch (DateTimeParseException e) {
            log.error("Неверный формат даты. Проверьте свою дату");
            return false;
        }
    }
}
