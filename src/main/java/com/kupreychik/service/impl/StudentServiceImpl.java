package com.kupreychik.service.impl;

import com.kupreychik.dto.request.StudentRequest;
import com.kupreychik.dto.response.ErrorResponse;
import com.kupreychik.dto.response.StudentResponse;
import com.kupreychik.exception.JsonParseException;
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

@Slf4j
public class StudentServiceImpl implements StudentService {
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
    public String getStudentById(Long id) throws JsonParseException {
        log.info("Выполняется поиск студента");
        try {
            Student student = studentRepository.getStudentById(id);
            log.info("Студент нашелся");
            return jsonParseService.writeToJson(student);
        } catch (Exception e) {
            return jsonParseService.writeToJson(new ErrorResponse("По данному id ничего не найдено"));
        }
    }

    @Override
    public String getStudentsBySurname(String surname, String name) throws JsonParseException {
        log.info("Выполняется поиск студента");
        try {
            Student student = studentRepository.getStudentBySurname(surname, name);
            log.info("Студент нашелся");
            return jsonParseService.writeToJson(student);
        } catch (Exception e) {
            return jsonParseService.writeToJson(new ErrorResponse("По данным surname и name ничего не найдено"));
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
    public String delete(StudentResponse studentResponse){
        log.info("deleteStudent() method call with value {}", studentResponse);

        Student studentToDelete = studentMapper.mapToModelResponse(studentResponse);
        var result = studentRepository.delete(studentToDelete);
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
