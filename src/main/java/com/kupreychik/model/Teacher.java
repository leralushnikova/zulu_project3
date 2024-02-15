package com.kupreychik.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Teacher extends AbstractModel{
    private Long id;
    private String name;
    private String surname;
    private Items item;
    private Integer experience;

}