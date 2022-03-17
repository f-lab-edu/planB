package com.flab.planb.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
@ToString
public class Member {

    private long id;
    @NotBlank(message = "사용자 ID는 Null과 공백이 아닌 값이 들어가야 합니다.")
    private String memberId;
    @Setter
    @NotBlank(message = "패스워드는 Null과 공백이 아닌 값이 들어가야 합니다.")
    private String passwd;
    @NotBlank(message = "닉네임은 Null과 공백이 아닌 값이 들어가야 합니다.")
    private String nickname;
    @NotBlank(message = "전화번호는 Null과 공백이 아닌 값이 들어가야 합니다.")
    private String tel;
    private String email;

    @Builder
    public Member(long id, String memberId, String passwd,
                  String nickname, String tel, String email) {
        this.id = id;
        this.memberId = memberId;
        this.passwd = passwd;
        this.nickname = nickname;
        this.tel = tel;
        this.email = email;
    }

}
