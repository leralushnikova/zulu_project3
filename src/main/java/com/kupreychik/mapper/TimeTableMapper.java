package com.kupreychik.mapper;

import com.kupreychik.dto.request.TimeTableRequest;
import com.kupreychik.dto.response.TimeTableResponse;
import com.kupreychik.model.TimeTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import static com.kupreychik.consts.RegexConsts.DATETIME_FORMAT;

@Mapper
public interface TimeTableMapper {

    TimeTableMapper INSTANCE = Mappers.getMapper(TimeTableMapper.class);

    @Mapping(target = "startDateTime", dateFormat = DATETIME_FORMAT)
    @Mapping(target = "endDateTime", dateFormat = DATETIME_FORMAT)
    TimeTable mapToModelRequest(TimeTableRequest dto);

    TimeTableResponse mapToResponse(TimeTable timeTable);
}