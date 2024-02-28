package com.kupreychik.dto.request;

import com.kupreychik.model.Items;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherRequest{
    private String name;
    private String surname;
    private String birthday;
    private String phoneNumber;
    private Items item;
    private Integer experience;
}