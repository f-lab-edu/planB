package com.flab.planb.response.message;

import com.flab.planb.response.ResponseResult;
import lombok.Getter;

@Getter
public enum MessageType {
    FAIL("error.fail.001", "FAIL_001", ResponseResult.ERROR),
    VALID_FAIL("error.valid.001", "VALID_FAIL_001", ResponseResult.ERROR),
    VALID_OVERLAP("error.valid.002", "VALID_FAIL_002", ResponseResult.ERROR),
    VALID_SUCCEED("succeed.valid.001", "VALID_SUCCEED", ResponseResult.SUCCEED),
    INSERT_FAIL_DATA("error.insert.001", "INSERT_FAIL_001", ResponseResult.ERROR),
    INSERT_SUCCEED("succeed.insert.001", "INSERT_SUCCEED", ResponseResult.SUCCEED),
    LOGIN_FAIL("error.login.001", "LOGIN_FAIL_001", ResponseResult.ERROR),
    DENIED_FAIL("error.access-denied", "DENIED_FAIL", ResponseResult.ERROR),
    AUTH_FAIL("error.authentication", "AUTH_FAIL", ResponseResult.ERROR),
    ILLEGAL_ARGUMENT_FAIL("error.illegalargument", "ILLEGAL_ARGUMENT_FAIL", ResponseResult.ERROR),
    BAD_REQUEST_FAIL("error.bac-request", "BAD_REQUEST_FAIL", ResponseResult.ERROR),
    SELECT_SUCCEED("succeed.select.001", "SELECT_SUCCEED", ResponseResult.SUCCEED),
    DELETE_SUCCEED("succeed.delete.001", "DELETE_SUCCEED", ResponseResult.SUCCEED),
    UPDATE_SUCCEED("succeed.update.001", "UPDATE_SUCCEED", ResponseResult.SUCCEED);

    private final String lookupKey;
    private final String code;
    private final ResponseResult responseResult;

    MessageType(String lookupKey, String code, ResponseResult responseResult) {
        this.lookupKey = lookupKey;
        this.code = code;
        this.responseResult = responseResult;
    }

}
