package com.flab.planb.dto.member.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import java.util.regex.Pattern;

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

    public static AddressRequestBuilder builder() {
        return new CustomBuilder();
    }

    private static class CustomBuilder extends AddressRequestBuilder {

        private static final Pattern pattern = Pattern.compile("[0-9]{5}");

        public AddressRequest build() {

            if (valid(super.zipCode, super.address)) {
                return super.build();
            }
            throw new IllegalArgumentException("AddressRequestBuilder is not valid");
        }

        private boolean valid(String zipCode, String address) {
            return (StringUtils.isEmpty(zipCode) && StringUtils.isEmpty(address))
                || (pattern.matcher(zipCode).matches() && StringUtils.isNotEmpty(address));
        }
    }
}
