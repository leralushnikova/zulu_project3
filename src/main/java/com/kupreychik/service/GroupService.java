package com.kupreychik.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kupreychik.dto.request.GroupRequestDouble;
import com.kupreychik.dto.response.GroupResponse;
import com.kupreychik.exception.JsonParseException;
import com.kupreychik.exception.ModelNotFound;

import java.util.List;

public interface GroupService {
    String getGroupByNumber(Long number) throws ModelNotFound, JsonParseException;
    String getGroupBySurnameOfStudent(String surname) throws ModelNotFound, JsonParseException;
    String addStudentInGroup(Long idGroup, Long idStudent) throws ModelNotFound, JsonParseException;
    String save(GroupRequestDouble groupRequest) throws JsonProcessingException;
    List<GroupResponse> getGroups();
}