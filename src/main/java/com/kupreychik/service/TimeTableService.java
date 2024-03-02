package com.kupreychik.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kupreychik.dto.request.TimeTableRequest;
import com.kupreychik.dto.response.TimeTableResponse;
import com.kupreychik.exception.JsonParseException;
import com.kupreychik.exception.ModelNotFound;

import java.util.List;

public interface TimeTableService {
    String getTimeTableByNumberOfGroup(Long groupNumber) throws ModelNotFound, JsonParseException;
    String getTimeTableBySurnameOfStudent(String surname) throws ModelNotFound, JsonParseException;
    String getTimeTableBySurnameOfTeacher(String surname) throws ModelNotFound, JsonParseException;
    String getTimeTableByDate(String date) throws JsonParseException;
    String changeTimeTableByDate(String changeHours, String date) throws JsonParseException;
    String save(TimeTableRequest groupRequest) throws JsonProcessingException;
    List<TimeTableResponse> getTimeTables();
}