package com.kupreychik.service.impl;

import com.kupreychik.dto.request.StudentRequest;
import com.kupreychik.dto.response.ErrorResponse;
import com.kupreychik.dto.response.StudentResponse;
import com.kupreychik.exception.ModelNotFound;
import com.kupreychik.mapper.StudentMapper;
import com.kupreychik.middleware.BirthdayMiddleware;
import com.kupreychik.middleware.Middleware;
import com.kupreychik.middleware.PhoneNumberMiddleware;
import com.kupreychik.model.Student;
import com.kupreychik.repository.StudentRepository;
import com.kupreychik.service.JsonParseService;
import com.kupreychik.service.StudentService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
public class StudentServiceImpl implements StudentService {
    private final String searchStudent = "Выполняется поиск студента по ";
    private final String foundStudent = "Студент нашелся";
    private final String error = "Ошибка. Такого студента нет";
    private final String notFoundStudent = "Cannot found student";
    private final StudentRepository studentRepository;
    private final JsonParseService jsonParseService;
    private final StudentMapper studentMapper;

    private final Middleware middleware = Middleware.link(
            new PhoneNumberMiddleware(),
            new BirthdayMiddleware()
    );

    public StudentServiceImpl(StudentRepository studentRepository, JsonParseService jsonParseService, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.jsonParseService = jsonParseService;
        this.studentMapper = studentMapper;
    }

    @Override
    @SneakyThrows
    public String getStudentById(Long id){
        log.info(searchStudent + "id = {}" , id);
        try {
            Student student = studentRepository.getStudentById(id);
            log.info(foundStudent);
            return jsonParseService.writeToJson(studentMapper.mapToResponse(student));
        } catch (ModelNotFound e) {
            log.error(error);
            return jsonParseService.writeToJson(new ErrorResponse(notFoundStudent));
        }
    }

    @Override
    @SneakyThrows
    public String getStudentBySurnameAndName(String surname, String name) {
        log.info(searchStudent + "surname = " + surname + " и name = " + name);
        try {
            Student student = studentRepository.getStudentBySurnameAndName(surname, name);
            log.info(foundStudent);
            return jsonParseService.writeToJson(studentMapper.mapToResponse(student));
        } catch (NoSuchElementException e) {
            log.error(error);
            return jsonParseService.writeToJson(new ErrorResponse(notFoundStudent));
        }
    }

    @Override
    @SneakyThrows
    public String save(StudentRequest studentRequest) {
        log.info("saveStudent() method call with value {}", studentRequest);

        if (!middleware.check(studentRequest)) {
            return jsonParseService.writeToJson(new ErrorResponse("Cannot create student"));
        }
        Student studentToSave = studentMapper.mapToModelRequest(studentRequest);
        var result = studentRepository.save(studentToSave);
        return jsonParseService.writeToJson(studentMapper.mapToResponse(result));
    }

    @Override
    @SneakyThrows
    public String delete(Long id){
        log.info(searchStudent + "id = {}" , id);

        try {
            Student studentToDelete = studentRepository.getStudentById(id);
            var result = studentRepository.delete(studentToDelete);
            log.info("Студента удалось удалить из списка");
            return jsonParseService.writeToJson(studentMapper.mapToResponse(result));
        } catch (ModelNotFound e) {
            log.error(error);
            return jsonParseService.writeToJson(new ErrorResponse(notFoundStudent));
        }
    }

    @Override
    @SneakyThrows
    public String change(Long id,StudentRequest studentRequest) {
        log.info("changeStudent() method call with value {}", studentRequest);

        if (!middleware.check(studentRequest)) {
            return jsonParseService.writeToJson(new ErrorResponse("Cannot change student"));
        }
        Student studentToChange = studentMapper.mapToModelRequest(studentRequest);
        var result = studentRepository.change(id, studentToChange);
        return jsonParseService.writeToJson(studentMapper.mapToResponse(result));
    }


    @Override
    public List<StudentResponse> getStudents() {
        log.info("getStudents() method call");
        return studentRepository.getAllStudents()
                .stream()
                .map(studentMapper::mapToResponse)
                .toList();
    }
}
