package com.flab.planb.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class AddressDTO {

    private long id;
    @Positive(message = "ID는 양수여야 합니다.")
    private long memberId;
    private String alias;
    @NotBlank(message = "우편변호는 Null과 공백이 아닌 값이 들어가야 합니다.")
    private String zipCode;
    @NotBlank(message = "주소는 Null과 공백이 아닌 값이 들어가야 합니다.")
    private String address;

    @Builder
    public AddressDTO(long id, long memberId, String alias, String zipCode, String address) {
        this.id = id;
        this.memberId = memberId;
        this.alias = alias;
        this.zipCode = zipCode;
        this.address = address;
    }
}
