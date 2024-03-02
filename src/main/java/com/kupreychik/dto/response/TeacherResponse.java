package com.kupreychik.dto.response;

import com.kupreychik.model.Items;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherResponse {
    private Long id;
    private String name;
    private String surname;
    private List<Items> items;
    private Integer experience;
    private LocalDate birthday;
    private String phoneNumber;
}