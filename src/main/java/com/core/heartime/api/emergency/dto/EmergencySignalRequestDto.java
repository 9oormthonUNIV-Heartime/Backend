package com.core.heartime.api.emergency.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmergencySignalRequestDto {

    // 클라가 좌표를 같이 줄 수도 있고 (선택), 안주면 서버가 최신 좌표를 조회
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    private Double latitude;

    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    private Double longitude;

    // 임의 신호값(선택): 심박 등. 심정지 테스트 시 0 전달
    private Double heartRate;

    // 상황 문자열(선택): "CARDIAC_ARREST" or "STROKE" 등, 있으면 그대로 사용
    private String situation;

    // 반경(선택). 미지정 시 1000m 고정
    @Positive
    private Integer radiusMeters;
}
