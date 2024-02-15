package com.kupreychik.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public abstract class AbstractModel {
    protected LocalDate birthday;
    protected String phone;
}