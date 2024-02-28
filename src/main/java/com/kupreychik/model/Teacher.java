package com.kupreychik.model;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Teacher{
    private Long id;
    private String name;
    private String surname;
    private List<Items> items;
    private Integer experience;
    private LocalDate birthday;
    private String phone;

}