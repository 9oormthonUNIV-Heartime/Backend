package com.core.heartime.api.emergency.controller;

import com.core.heartime.api.emergency.dto.EmergencySignalRequestDto;
import com.core.heartime.api.emergency.service.EmergencyService;
import com.core.heartime.common.response.ApiResponse;
import com.core.heartime.common.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/emergency")
public class EmergencyController {

    private final EmergencyService emergencyService;

    @Operation(
            summary = "생체신호 수신/응급 판정",
            description = "심정지(심박 0) 또는 situation 지정 시 응급으로 판정하고, 반경 1km 이내 HELPER들에게 알림을 발송합니다. " +
                    "위도/경도가 없으면 서버가 최신 위치를 사용합니다."
    )
    @PostMapping("/signal")
    public ResponseEntity<ApiResponse<Map<String, Object>>> signal(
            // 로그인된 사용자 정보를 주입함
            // 여기서 막하는 username은 그냥 식별자임 id가 아니라 그래서 그냥 이메일을 넣은것과 같은거임
            //SpEL(expression)은 객체의 프로퍼티명/메서드명을 기준으로 동작해서 그런것
            @AuthenticationPrincipal(expression = "username") String email,
            @Valid @RequestBody EmergencySignalRequestDto request
    ) {
        // email : 누가 신고자인지 식별 / request : 클라이언트가 보낸 응급 신호 데이터
        Map<String, Object> result = emergencyService.processSignal(email, request);
        return ApiResponse.success(SuccessStatus.EMERGENCY_SIGNAL_HANDLED, result);
    }
}
