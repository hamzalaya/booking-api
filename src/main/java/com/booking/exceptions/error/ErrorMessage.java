package com.booking.exceptions.error;

public class ErrorMessage {
    private String message;
    private String code;

    public ErrorMessage() {
    }

    public ErrorMessage(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public ErrorMessage setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getCode() {
        return code;
    }

    public ErrorMessage setCode(String code) {
        this.code = code;
        return this;
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "message='" + message + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
