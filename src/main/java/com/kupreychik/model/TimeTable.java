package com.kupreychik.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeTable {
    private Long groupId;
    private Long teacherId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}