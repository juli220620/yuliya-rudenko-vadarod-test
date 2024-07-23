package com.github.juli220620.service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoadStatus {

    private int code;
    private String errorMessage;

    public LoadStatus() {
        code = 0;
        errorMessage = "OK";
    }

    public LoadStatus(Exception e) {
        code = 500;
        errorMessage = e.getMessage();
    }
}
