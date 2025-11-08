package com.booking.exceptions.error;

import java.util.Objects;

public class FieldError {

    private String code;
    private String field;
    private String rejectedValue;
    private String message;

    public String getCode() {
        return code;
    }

    public FieldError setCode(String code) {
        this.code = code;
        return this;
    }

    public String getField() {
        return field;
    }

    public FieldError setField(String field) {
        this.field = field;
        return this;
    }

    public String getRejectedValue() {
        return rejectedValue;
    }

    public FieldError setRejectedValue(String rejectedValue) {
        this.rejectedValue = rejectedValue;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public FieldError setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public String toString() {
        return "FieldError{" +
                "code='" + code + '\'' +
                ", field='" + field + '\'' +
                ", rejectedValue='" + rejectedValue + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldError that = (FieldError) o;
        return Objects.equals(code, that.code) && Objects.equals(field, that.field) && Objects.equals(rejectedValue, that.rejectedValue) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, field, rejectedValue, message);
    }
}
