package com.kupreychik.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kupreychik.dto.request.StudentRequest;
import com.kupreychik.dto.response.StudentResponse;

import java.util.List;

public interface StudentService {
    StudentResponse getStudent(Long id);

    String save(StudentRequest studentRequest) throws JsonProcessingException;

    List<StudentResponse> getStudents();
}