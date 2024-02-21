package com.kupreychik.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentRequest{
    private String name;
    private String surname;
    private String birthday;
    private String phoneNumber;
}