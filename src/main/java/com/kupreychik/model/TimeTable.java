package com.kupreychik.model;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TimeTable {
    private String date;
    private Group group;
    private Teacher teacher;
}