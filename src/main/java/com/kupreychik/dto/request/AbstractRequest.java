package com.kupreychik.dto.request;

import lombok.*;

import java.time.LocalDate;

@Getter
public abstract class AbstractRequest {
    protected String birthday;
    protected String phoneNumber;
}