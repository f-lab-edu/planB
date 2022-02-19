package com.flab.planb.message;

public enum MessageCode {
    VALID_FAIL("error.valid.001", "VALID_FAIL_001"),
    VALID_OVERLAP("error.valid.002", "VALID_FAIL_002"),
    VALID_SUCCEED("succeed.valid.001", "VALID_SUCCEED"),
    INSERT_FAIL_DATA("error.insert.001", "INSERT_FAIL_001"),
    INSERT_SUCCEED("succeed.insert.001", "INSERT_SUCCEED");

    private final String key;
    private final String value;

    MessageCode(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }
}
