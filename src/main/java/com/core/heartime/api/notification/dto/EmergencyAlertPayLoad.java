package com.core.heartime.api.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

public class EmergencyAlertPayLoad {
    Long eventId;
    double latitude;
    double longitude;
    String emergencyType;  // 예: CARDIAC_ARREST, STROKE
    String title;         // [응급] 가까운 곳에서 도움이 필요합니다
    String message;        // 조치 요약 문구
    List<String> imageUrls;

    public EmergencyAlertPayload() {}

    public EmergencyAlertPayload(Long eventId, double latitude, double longitude,
                                 String emergencyType, String title, String message,
                                 List<String> imageUrls) {
        this.eventId = eventId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.emergencyType = emergencyType;
        this.title = title;
        this.message = message;
        this.imageUrls = imageUrls;
    }

    public Long getEventId() { return eventId; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getEmergencyType() { return emergencyType; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public List<String> getImageUrls() { return imageUrls; }
}
