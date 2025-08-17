package com.core.heartime.api.members.controller;

import com.core.heartime.api.members.dto.LoginRequestDto;
import com.core.heartime.api.members.dto.SignupRequestDto;
import com.core.heartime.api.members.service.MemberService;
import com.core.heartime.common.response.ApiResponse;
import com.core.heartime.common.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "Member", description = "Member 관련 API 입니다.")
@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
@Validated
public class MemberController {
    private final MemberService memberService;

    @Operation(
            summary = "회원가입",
            description = "사용자 정보를 등록합니다."
    )
    @PostMapping("/auth/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@Valid @RequestBody SignupRequestDto request) {
        // valid : 유효성 검사(@NotNull, @Size, @Email 등)
        // request : dto에 매핑
        memberService.signupMember(request);

        return ApiResponse.successOnly(SuccessStatus.MEMBER_SIGNUP_SUCCESS);
    }

    @Operation(
            summary = "로그인",
            description = "사용자의 정보를 확인 및 성공 시 토큰을 발급합니다."
    )
    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@Valid @RequestBody LoginRequestDto request) {
        Map<String, Object> result = memberService.loginMember(request);

        return ApiResponse.success(SuccessStatus.LOGIN_SUCCESS, result);
    }
}
