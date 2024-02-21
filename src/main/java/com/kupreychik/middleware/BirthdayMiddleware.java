package com.kupreychik.middleware;

import com.kupreychik.dto.request.StudentRequest;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.kupreychik.consts.RegexConsts.DATE_FORMAT;

@Slf4j
public class BirthdayMiddleware extends Middleware {
    @Override
    public boolean check(StudentRequest model) {
        log.debug("Выполняется проверка даты рождения");
        try {
            LocalDate.parse(model.getBirthday(), DateTimeFormatter.ofPattern(DATE_FORMAT));
            log.debug("Проверка пройдена. Дата подходит");
            return checkNext(model);
        } catch (DateTimeParseException e) {
            log.debug("неверный формат даты. Проверьте свою дату");
            return false;
        }
    }
}
