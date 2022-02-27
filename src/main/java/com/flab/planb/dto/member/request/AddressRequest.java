package com.flab.planb.dto.member.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class AddressRequest {

    @Setter
    private long id;
    @Setter
    private long memberId;
    private String alias;
    private String zipCode;
    private String address;

    @Builder
    public AddressRequest(String alias, String zipCode, String address) {
        this.alias = alias;
        this.zipCode = zipCode;
        this.address = address;
    }
}
