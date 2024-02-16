package com.kupreychik.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kupreychik.dto.request.StudentRequest;
import com.kupreychik.dto.response.StudentResponse;
import com.kupreychik.exception.JsonParseException;
import com.kupreychik.exception.ModelNotFound;

import java.util.List;

public interface StudentService {
    String getStudentById(Long id) throws ModelNotFound, JsonParseException;
    String getStudentsBySurname(String surname, String name) throws ModelNotFound, JsonParseException;

    String save(StudentRequest studentRequest) throws JsonProcessingException;
    String delete(StudentResponse studentResponse) throws JsonProcessingException;

    List<StudentResponse> getStudents();
}