package com.flab.planb.dto.member;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDTO {

    @NonNull
    private String id;
    @NonNull
    private String passwd;
    @NonNull
    private String nickname;
    @NonNull
    private String tel;
    private String email;

}
