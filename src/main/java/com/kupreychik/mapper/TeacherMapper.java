package com.kupreychik.mapper;

import com.kupreychik.dto.request.TeacherRequest;
import com.kupreychik.dto.response.TeacherResponse;
import com.kupreychik.model.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TeacherMapper {

    TeacherMapper INSTANCE = Mappers.getMapper(TeacherMapper.class);

    Teacher mapToModelRequest(TeacherRequest dto);

    TeacherResponse mapToResponse(Teacher teacher);
    Teacher mapToModelResponse(TeacherResponse dto);

}