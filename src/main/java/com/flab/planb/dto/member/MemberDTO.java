package com.flab.planb.dto.member;

import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class MemberDTO {
    public MemberDTO() {
    }
    
    public MemberDTO(long id, String userId, String passwd, String nickname, String tel, String email) {
        this.id = id;
        this.userId = userId;
        this.passwd = passwd;
        this.nickname = nickname;
        this.tel = tel;
        this.email = email;
    }
    
    private long id;
    @NotBlank(message = "사용자 ID는 Null과 공백이 아닌 값이 들어가야 합니다.")
    private String userId;
    @NotBlank(message = "패스워드는 Null과 공백이 아닌 값이 들어가야 합니다.")
    private String passwd;
    @NotBlank(message = "닉네임은 Null과 공백이 아닌 값이 들어가야 합니다.")
    private String nickname;
    @NotBlank(message = "전화번호는 Null과 공백이 아닌 값이 들어가야 합니다.")
    private String tel;
    private String email;
    
}
