package com.kupreychik.middleware;

import com.kupreychik.dto.request.AbstractRequest;
import lombok.extern.slf4j.Slf4j;

import static com.kupreychik.consts.RegexConsts.PHONE_REGEX;

@Slf4j
public class PhoneNumberMiddleware extends Middleware {
    @Override
    public boolean check(AbstractRequest model) {
        log.debug("Выполняется проверка номера телефона");
        if (model.getPhoneNumber().matches(PHONE_REGEX)) {
            log.debug("Проверка пройдена. Телефон подходит");
            return checkNext(model);
        } else {
            log.error("Неверный формат даты. Проверьте свою дату");
            return false;
        }
    }
}
