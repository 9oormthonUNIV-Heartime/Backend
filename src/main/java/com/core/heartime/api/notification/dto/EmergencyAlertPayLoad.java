package com.core.heartime.api.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class EmergencyAlertPayLoad {
    Long eventId;
    double latitude;
    double longitude;
    String emergencyType;  // 예: CARDIAC_ARREST, STROKE
    String title;         // [응급] 가까운 곳에서 도움이 필요합니다
    String message;        // 조치 요약 문구
    List<String> imageUrls;
}
