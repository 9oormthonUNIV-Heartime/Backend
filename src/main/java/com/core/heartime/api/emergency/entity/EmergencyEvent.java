package com.core.heartime.api.emergency.entity;

import com.core.heartime.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Builder(toBuilder = true)
@Table(name = "emergency_event")
public class EmergencyEvent extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 신고자 이메일
    @Column(name = "reporter_id", nullable = false, length = 50)
    private String reporterId;

    // 위도
    @Column(nullable = false)
    private Double latitude;

    // 경도
    @Column(nullable = false)
    private Double longitude;

    // 심박수 (선택)
    @Column
    private Double heartRate;

    // 응급 상황 타입: 예) CARDIAC_ARREST, STROKE
    @Column(name = "emergency_type", nullable = false, length = 30)
    private String emergencyType;

    // 지역 필터용 도/시/구 코드 또는 문자열 (선택)
    @Column(name = "region_code")
    private String regionCode;


}
