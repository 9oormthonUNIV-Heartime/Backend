package com.core.heartime.api.members.service;

import com.core.heartime.api.members.dto.LoginRequestDto;
import com.core.heartime.api.members.dto.SignupRequestDto;
import com.core.heartime.api.members.entity.Member;
import com.core.heartime.api.members.jwt.JwtTokenProvider;
import com.core.heartime.api.members.repository.MemberRepository;
import com.core.heartime.common.exception.BaseException;
import com.core.heartime.common.exception.UnauthorizedException;
import com.core.heartime.common.response.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void signupMember(SignupRequestDto request) {

        //이미 사용 중인 이메일입니다.
        if(memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BaseException(ErrorStatus.BAD_REQUEST_DUPLICATE_EMAIL.getHttpStatus(),
                    ErrorStatus.BAD_REQUEST_DUPLICATE_EMAIL.getMessage());
        }
        if(memberRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            throw new BaseException(HttpStatus.BAD_REQUEST, ErrorStatus.BAD_REQUEST_DUPLICATE_PHONE.getMessage());
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Member member = request.toEntity(encodedPassword);
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> loginMember(LoginRequestDto request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(()->new UnauthorizedException(ErrorStatus.UNAUTHORIZED_EMAIL_OR_PASSWORD.getMessage()));

        if(!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new UnauthorizedException(ErrorStatus.UNAUTHORIZED_EMAIL_OR_PASSWORD.getMessage());
        }

        String token = jwtTokenProvider.generateToken(member.getEmail(), member.getRole());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("role", member.getRole());
        return response;

    }
}
