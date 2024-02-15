package com.kupreychik.model;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Student extends AbstractModel{
    private Long id;
    private String name;
    private String surname;
}