package com.flab.planb.message;

public enum MessageCode {
    VALID_FAIL("error.valid.001", "VALID_FAIL_001"),
    VALID_OVERLAP("error.valid.002", "VALID_FAIL_002"),
    VALID_SUCCEED("succeed.valid.001", "VALID_SUCCEED"),
    INSERT_FAIL_DATA("error.insert.001", "INSERT_FAIL_001"),
    INSERT_SUCCEED("succeed.insert.001", "INSERT_SUCCEED"),
    LOGIN_FAIL("error.login.001", "LOGIN_FAIL_001"),
    DENIED_FAIL("error.access-denied", "DENIED_FAIL"),
    AUTH_FAIL("error.authentication", "AUTH_FAIL"),
    ILLEGAL_ARGUMENT_FAIL("error.illegalargument", "ILLEGAL_ARGUMENT_FAIL"),
    BAD_REQUEST_FAIL("error.bac-request", "BAD_REQUEST_FAIL");

    private final String messageKey;
    private final String messageCode;

    MessageCode(String messageKey, String messageCode) {
        this.messageKey = messageKey;
        this.messageCode = messageCode;
    }

    public String getMessageKey() {
        return this.messageKey;
    }

    public String getMessageCode() {
        return this.messageCode;
    }
}
