package com.core.heartime.api.members.dto;

import com.core.heartime.api.members.entity.Member;
import com.core.heartime.api.members.entity.MemberType;
import com.core.heartime.api.members.entity.Role;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignupRequestDto {

    @NotBlank(message = "닉네임을 필수입니다.")
    private String nickname;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "전화번호는 필수입니다.")
    private String phoneNumber;

    @Min(value = 0, message = "나이는 0 이상이어야 합니다.")
    @Max(value = 150, message = "나이는 150 이하이어야 합니다.")
    private int age;

    @NotNull(message = "권한은 필수입니다.")
    private Role role;

    @NotNull(message = "신분은 필수입니다.")
    private MemberType memberType;

    //DTO -> Member Entity
    public Member toEntity(String encodedPassword) {
        return Member.builder()
                .nickname(nickname)
                .password(encodedPassword)
                .email(email)
                .phoneNumber(phoneNumber)
                .age(age)
                .role(role)
                .memberType(memberType)
                .build();
    }
}
