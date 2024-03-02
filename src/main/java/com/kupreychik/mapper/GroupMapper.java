package com.kupreychik.mapper;

import com.kupreychik.dto.request.GroupRequest;
import com.kupreychik.dto.response.GroupResponse;
import com.kupreychik.model.Group;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GroupMapper {

    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    Group mapToModelRequest(GroupRequest dto);

    GroupResponse mapToResponse(Group group);

}