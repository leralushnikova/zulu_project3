package com.kupreychik.model;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    private Long id;
    private Long number;
    private List<Student> students;
}