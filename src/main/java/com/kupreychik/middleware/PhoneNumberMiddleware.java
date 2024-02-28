package com.kupreychik.middleware;

import com.kupreychik.dto.request.StudentRequest;
import com.kupreychik.dto.request.TeacherRequest;
import lombok.extern.slf4j.Slf4j;

import static com.kupreychik.consts.RegexConsts.PHONE_REGEX;

@Slf4j
public class PhoneNumberMiddleware extends Middleware {
    @Override
    public boolean check(Object model) {
        log.debug("Выполняется проверка номера телефона");
        String testPhoneNumber = null;
        if (model instanceof StudentRequest studentRequest) {
            testPhoneNumber = studentRequest.getPhoneNumber();
        }
        if (model instanceof TeacherRequest teacherRequest) {
            testPhoneNumber = teacherRequest.getPhoneNumber();
        }
        assert testPhoneNumber != null;
        return getTestPhoneNumber(testPhoneNumber, model);
    }

    private boolean getTestPhoneNumber(String string, Object object) {
        if(string.matches(PHONE_REGEX)){
            log.debug("Проверка пройдена. Телефон подходит");
            return checkNext(object);
        }
        log.error("Неверный формат даты. Проверьте свою дату");
        return false;
    }

}
