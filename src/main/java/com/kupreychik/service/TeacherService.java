package com.kupreychik.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kupreychik.dto.request.TeacherRequest;
import com.kupreychik.dto.response.TeacherResponse;
import com.kupreychik.exception.JsonParseException;
import com.kupreychik.exception.ModelNotFound;

import java.util.List;

public interface TeacherService {
    String getTeacherById(Long id) throws ModelNotFound, JsonParseException;
    String getStudentsBySurname(String surname, String name) throws ModelNotFound, JsonParseException;

    String save(TeacherRequest studentRequest) throws JsonProcessingException;
    String delete(TeacherResponse studentResponse) throws JsonProcessingException;

    List<TeacherResponse> getStudents();
}