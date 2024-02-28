package com.kupreychik.model;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Student{
    private Long id;
    private String name;
    private String surname;
    private LocalDate birthday;
    private String phone;
}