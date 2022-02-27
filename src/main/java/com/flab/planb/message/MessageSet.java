package com.flab.planb.message;

public enum MessageSet {
    VALID_FAIL("error.valid.001", "VALID_FAIL_001"),
    VALID_OVERLAP("error.valid.002", "VALID_FAIL_002"),
    VALID_SUCCEED("succeed.valid.001", "VALID_SUCCEED"),
    INSERT_FAIL_DATA("error.insert.001", "INSERT_FAIL_001"),
    INSERT_SUCCEED("succeed.insert.001", "INSERT_SUCCEED"),
    SELECT_SUCCEED("succeed.select.001", "SELECT_SUCCEED"),
    DELETE_SUCCEED("succeed.delete.001", "DELETE_SUCCEED"),
    UPDATE_SUCCEED("succeed.update.001", "UPDATE_SUCCEED");

    private final String lookupKey;
    private final String code;

    MessageSet(String lookupKey, String code) {
        this.lookupKey = lookupKey;
        this.code = code;
    }

    public String getLookupKey() {
        return this.lookupKey;
    }

    public String getCode() {
        return this.code;
    }
}
