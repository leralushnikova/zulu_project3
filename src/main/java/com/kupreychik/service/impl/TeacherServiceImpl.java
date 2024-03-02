package com.kupreychik.service.impl;

import com.kupreychik.dto.request.TeacherRequest;
import com.kupreychik.dto.response.ErrorResponse;
import com.kupreychik.dto.response.TeacherResponse;
import com.kupreychik.exception.ModelNotFound;
import com.kupreychik.mapper.TeacherMapper;
import com.kupreychik.middleware.BirthdayMiddleware;
import com.kupreychik.middleware.Middleware;
import com.kupreychik.middleware.PhoneNumberMiddleware;
import com.kupreychik.model.Teacher;
import com.kupreychik.repository.TeacherRepository;
import com.kupreychik.service.JsonParseService;
import com.kupreychik.service.TeacherService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
public class TeacherServiceImpl implements TeacherService {
    private final String error = "Ошибка. Такого учителя нет";
    private final String notFoundTeacher = "Cannot found teacher";
    private final TeacherRepository teacherRepository;
    private final JsonParseService jsonParseService;
    private final TeacherMapper teacherMapper;

    private final Middleware middleware = Middleware.link(
            new PhoneNumberMiddleware(),
            new BirthdayMiddleware()
    );

    public TeacherServiceImpl(TeacherRepository teacherRepository, JsonParseService jsonParseService, TeacherMapper teacherMapper) {
        this.teacherRepository = teacherRepository;
        this.jsonParseService = jsonParseService;
        this.teacherMapper = teacherMapper;
    }

    @Override
    @SneakyThrows
    public String save(TeacherRequest teacherRequest) {
        log.info("saveTeacher() method call with value {}", teacherRequest);

        if (!middleware.check(teacherRequest)) {
            return jsonParseService.writeToJson(new ErrorResponse("Cannot create teacher"));
        }
        Teacher teacherToSave = teacherMapper.mapToModelRequest(teacherRequest);
        var result = teacherRepository.save(teacherToSave);
        return jsonParseService.writeToJson(teacherMapper.mapToResponse(result));
    }

    @Override
    @SneakyThrows
    public String delete(Long id){
        log.info("Выполняется поиск учителя");

        try {
            Teacher teacherToDelete = teacherRepository.getTeacherById(id);
            var result = teacherRepository.delete(teacherToDelete);
            log.info("Учителя удалось удалить из списка");
            return jsonParseService.writeToJson(teacherMapper.mapToResponse(result));
        } catch (ModelNotFound e) {
            log.error(error);
            return jsonParseService.writeToJson(new ErrorResponse(notFoundTeacher));
        }
    }

    @Override
    @SneakyThrows
    public String change(Long id, String item) {
        log.info("changeTeacher() method call with value {}", item);

        try {
            Teacher teacherToChange = teacherRepository.getTeacherById(id);
            var result = teacherRepository.change(id, teacherToChange, item);
            String foundTeacher = "Учитель нашелся";
            log.info(foundTeacher);
            return jsonParseService.writeToJson(teacherMapper.mapToResponse(result));
        } catch (NoSuchElementException e) {
            log.error(error);
            return jsonParseService.writeToJson(new ErrorResponse(notFoundTeacher));
        }
    }


    @Override
    public List<TeacherResponse> getTeachers() {
        log.info("getTeachers() method call");
        return teacherRepository.getAllTeachers()
                .stream()
                .map(teacherMapper::mapToResponse)
                .toList();
    }
}
