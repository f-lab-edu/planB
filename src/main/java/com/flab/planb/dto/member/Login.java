package com.flab.planb.dto.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
@ToString
public class Login {

    @NotBlank
    private String memberId;
    @NotBlank
    private String passwd;
    @Setter
    private String nickname;

    public Login(String memberId, String passwd) {
        this.memberId = memberId;
        this.passwd = passwd;
    }

}
