package com.kupreychik.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kupreychik.dto.request.StudentRequest;
import com.kupreychik.dto.response.StudentResponse;
import com.kupreychik.exception.JsonParseException;
import com.kupreychik.exception.ModelNotFound;

import java.util.List;

public interface StudentService {
    String getStudentById(Long id) throws ModelNotFound, JsonParseException;
    String getStudentBySurnameAndName(String surname, String name) throws ModelNotFound, JsonParseException;


    String save(StudentRequest studentRequest) throws JsonProcessingException;
    String delete(Long id) throws JsonProcessingException;
    String change(Long id, StudentRequest studentRequest) throws JsonProcessingException;

    List<StudentResponse> getStudents();
}