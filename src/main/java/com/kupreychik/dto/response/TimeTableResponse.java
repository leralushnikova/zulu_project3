package com.kupreychik.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TimeTableResponse {
    private Long groupId;
    private Long teacherId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}