package com.flab.planb.message;

import com.flab.planb.type.Result;
import lombok.Getter;

@Getter
public enum MessageSet {
    FAIL("error.fail.001", "FAIL_001", Result.ERROR),
    VALID_FAIL("error.valid.001", "VALID_FAIL_001", Result.ERROR),
    VALID_OVERLAP("error.valid.002", "VALID_FAIL_002", Result.ERROR),
    VALID_SUCCEED("succeed.valid.001", "VALID_SUCCEED", Result.SUCCEED),
    INSERT_FAIL_DATA("error.insert.001", "INSERT_FAIL_001", Result.ERROR),
    INSERT_SUCCEED("succeed.insert.001", "INSERT_SUCCEED", Result.SUCCEED),
    LOGIN_FAIL("error.login.001", "LOGIN_FAIL_001", Result.ERROR),
    DENIED_FAIL("error.access-denied", "DENIED_FAIL", Result.ERROR),
    AUTH_FAIL("error.authentication", "AUTH_FAIL", Result.ERROR),
    ILLEGAL_ARGUMENT_FAIL("error.illegalargument", "ILLEGAL_ARGUMENT_FAIL", Result.ERROR),
    BAD_REQUEST_FAIL("error.bac-request", "BAD_REQUEST_FAIL", Result.ERROR),
    SELECT_SUCCEED("succeed.select.001", "SELECT_SUCCEED", Result.SUCCEED),
    DELETE_SUCCEED("succeed.delete.001", "DELETE_SUCCEED", Result.SUCCEED),
    UPDATE_SUCCEED("succeed.update.001", "UPDATE_SUCCEED", Result.SUCCEED);

    private final String lookupKey;
    private final String code;
    private final Result result;

    MessageSet(String lookupKey, String code, Result result) {
        this.lookupKey = lookupKey;
        this.code = code;
        this.result = result;
    }

}
