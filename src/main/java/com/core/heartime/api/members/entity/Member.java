package com.core.heartime.api.members.entity;

import com.core.heartime.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
@Table(name = "members")
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //닉네임
    @Column(name = "nickname", nullable = false, unique = true, length = 20)
    private String nickname;

    //비밀번호
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    //이메일 -> 이걸로 구분할 예정
    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email;

    //핸드폰 번호
    @Column(name = "phone_number", nullable = false, unique = true, length = 11)
    private String phoneNumber;

    //나이
    @Column(name = "age")
    private int age;

    //헬피, 헬포로 넣어놓을생각
    @Enumerated(EnumType.STRING)
    @Column(name = "member_type", nullable = false)
    private MemberType memberType;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;


}
