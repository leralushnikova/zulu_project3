package com.kupreychik.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kupreychik.dto.request.StudentRequest;
import com.kupreychik.dto.response.ErrorResponse;
import com.kupreychik.dto.response.StudentResponse;
import com.kupreychik.mapper.StudentMapper;
import com.kupreychik.model.Student;
import com.kupreychik.repository.StudentRepository;
import com.kupreychik.service.StudentService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

@Slf4j
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final JsonParseServiceImpl jsonParseService;
    private final StudentMapper studentMapper;

    public StudentServiceImpl(StudentRepository studentRepository, JsonParseServiceImpl jsonParseService, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.jsonParseService = jsonParseService;
        this.studentMapper = studentMapper;
    }

    @Override
    public StudentResponse getStudent(Long id) {
        for (StudentResponse student : getStudents()) {
            if (id.equals(student.getId())) {
                return student;
            }
        }
        return null;
    }

    @Override
    @SneakyThrows
    public String save(StudentRequest studentRequest) throws JsonProcessingException {
        log.info("saveStudent() method call with value {}", studentRequest);

        Student studentToSave = studentMapper.mapToModel(studentRequest);
        var result = studentRepository.save(studentToSave);
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
