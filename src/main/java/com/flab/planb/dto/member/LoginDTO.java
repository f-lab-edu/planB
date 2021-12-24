package com.flab.planb.dto.member;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class LoginDTO {

    @NotBlank
    private String memberId;
    @NotBlank
    private String passwd;
    private String nickname;

    @Builder
    public LoginDTO(String memberId, String passwd, String nickname) {
        this.memberId = memberId;
        this.passwd = passwd;
        this.nickname = nickname;
    }

}
