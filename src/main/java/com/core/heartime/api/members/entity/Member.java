package com.core.heartime.api.members.entity;

import com.core.heartime.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor // 모든 필드를 받아서 한번에 객체를 만들기 위해
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 비어있는 필드로 만들고 거기에 원하는 필드를 넣기 위해
@Builder(toBuilder = true) // toBuilder = true하면 한번 선언한 객체를 다시 다른것로 선언가능
@Table(name = "members")
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK값을 어떻게 생성할건지 DB에게 위임, 이제 알아서 1, 2, 3... 이런식으로 생성
    private Long id;

    //닉네임
    // nullable = false : 반드시 있어야함 / unique = true : 중복된 값 허용 안됨.
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
    // Enum의 이름 그대로 문자열을 DB에 저장. / .ORDER로 하면 새로운값 추가 됐을때 선언이 다르게 될 수 있음.
    @Enumerated(EnumType.STRING)
    @Column(name = "member_type", nullable = false)
    private MemberType memberType;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;


}
