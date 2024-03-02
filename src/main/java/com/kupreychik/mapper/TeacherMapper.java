package com.kupreychik.mapper;

import com.kupreychik.dto.request.TeacherRequest;
import com.kupreychik.dto.response.TeacherResponse;
import com.kupreychik.model.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TeacherMapper {

    TeacherMapper INSTANCE = Mappers.getMapper(TeacherMapper.class);

    @Mapping(target = "phone", source = "phoneNumber")
    Teacher mapToModelRequest(TeacherRequest dto);

    @Mapping(target = "phoneNumber", source = "phone")
    TeacherResponse mapToResponse(Teacher teacher);
}