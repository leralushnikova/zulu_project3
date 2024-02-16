package com.kupreychik.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
public abstract class AbstractModel {
    protected LocalDate birthday;
    protected String phoneNumber;
}