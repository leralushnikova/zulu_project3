package com.kupreychik.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kupreychik.dto.request.TeacherRequest;
import com.kupreychik.dto.response.TeacherResponse;
import com.kupreychik.exception.JsonParseException;
import com.kupreychik.exception.ModelNotFound;
import com.kupreychik.model.Items;

import java.util.List;

public interface TeacherService {
    String getTeacherById(Long id) throws ModelNotFound, JsonParseException;
    String getTeacherBySurname(String surname, String name) throws ModelNotFound, JsonParseException;

    String save(TeacherRequest teacherRequest) throws JsonProcessingException;
    String delete(Long id) throws JsonProcessingException;
    String change(Long id, String item) throws JsonProcessingException;

    List<TeacherResponse> getTeachers();
}