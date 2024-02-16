package com.kupreychik.mapper;

import com.kupreychik.dto.request.StudentRequest;
import com.kupreychik.dto.response.StudentResponse;
import com.kupreychik.model.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StudentMapper {

    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);


    Student mapToModelRequest(StudentRequest dto);

    StudentResponse mapToResponse(Student student);
    Student mapToModelResponse(StudentResponse dto);

}