package com.kupreychik.model;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Teacher{
    private Long id;
    private String name;
    private String surname;
    private Items item;
    private Integer experience;
    private LocalDate birthday;
    private String phone;

}