package com.kupreychik.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeTableRequest {
    private Long groupId;
    private Long teacherId;
    private String startDateTime;
    private String endDateTime;
}