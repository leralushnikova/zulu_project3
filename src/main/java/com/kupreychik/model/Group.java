package com.kupreychik.model;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    private List<Student> students;
    private Long id;
    private String number;
}