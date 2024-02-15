package com.kupreychik.dto.request;

import lombok.Getter;

@Getter
public abstract class AbstractRequest {
    protected String birthday;
    protected String phoneNumber;
}