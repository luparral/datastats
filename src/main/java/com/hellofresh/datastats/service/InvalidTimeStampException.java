package com.hellofresh.datastats.service;

public class InvalidTimeStampException extends Exception {
    private String code;

    public InvalidTimeStampException(String code, String message){
        super(message);
        this.setCode(code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
