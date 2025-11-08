package com.booking.exceptions.error;

public enum ExceptionCode {
    API_BAD_REQUEST(400, "error.api.bad.request"),
    API_VALIDATION(400, "error.api.validation"),
    API_NOT_FOUND(404, "error.api.not.found"),
    API_UNAUTHORIZED(401, "error.api.unauthorized"),
    API_FORBIDDEN(403, "error.api.forbidden"),
    API_INTERNAL_SERVER_ERROR(500, "error.api.internal.server.error");

    private final int status;

    private final String code;

    ExceptionCode(int status, String code) {
        this.status = status;
        this.code = code;
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}
