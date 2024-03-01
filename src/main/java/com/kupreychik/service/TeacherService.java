package com.kupreychik.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kupreychik.dto.request.TeacherRequest;
import com.kupreychik.dto.response.TeacherResponse;

import java.util.List;

public interface TeacherService {
    String save(TeacherRequest teacherRequest) throws JsonProcessingException;
    String delete(Long id) throws JsonProcessingException;
    String change(Long id, String item) throws JsonProcessingException;

    List<TeacherResponse> getTeachers();
}