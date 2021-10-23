package com.flab.planb.message;

public enum MessageCode {
    VALID_FAIL("error.valid.001", "VALID_FAIL_001"),
    VALID_OVERLAP("error.valid.002", "VALID_FAIL_002"),
    VALID_SUCCEED("succeed.valid.001", "VALID_SUCCEED"),
    INSERT_FAIL_DATA("error.insert.001", "INSERT_FAIL_001"),
    INSERT_SUCCEED("succeed.insert.001", "INSERT_SUCCEED");

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
