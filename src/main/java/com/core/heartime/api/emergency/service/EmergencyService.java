package com.core.heartime.api.emergency.service;


import com.core.heartime.api.emergency.dto.EmergencySignalRequestDto;
import com.core.heartime.api.emergency.entity.EmergencyEvent;
import com.core.heartime.api.emergency.repository.EmergencyEventRepository;
import com.core.heartime.api.emergency.rule.EmergencyRuleEngine;
import com.core.heartime.api.location.entity.MemberLocation;
import com.core.heartime.api.location.geo.RedisGeoService;
import com.core.heartime.api.location.repository.MemberLocationRepository;
import com.core.heartime.api.members.entity.Member;
import com.core.heartime.api.members.entity.MemberType;
import com.core.heartime.api.members.entity.Role;
import com.core.heartime.api.members.repository.MemberRepository;
import com.core.heartime.api.notification.dto.EmergencyAlertPayLoad;
import com.core.heartime.api.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmergencyService {

    private final EmergencyEventRepository eventRepository;
    private final MemberLocationRepository memberLocationRepository;
    private final MemberRepository memberRepository;

    private final RedisGeoService geo;
    private final NotificationService notificationService;
    private final EmergencyRuleEngine ruleEngine;

    /**
     * 임의 신호를 받아 응급상황이면 반경 1km(기본) HELPER들에게 알림
     * - 좌표가 요청에 없으면 최신 위치를 사용
     * - situation/heartRate로 간단 판정
     */
    @Transactional
    public Map<String, Object> processSignal(String reporterEmail, EmergencySignalRequestDto request) {
        // 1) 응급 판정
        String type = ruleEngine.decideType(request.getHeartRate(), request.getSituation());
        if (type == null) {
            // 응급 아님 -> 바로 리턴
            Map<String, Object> res = new HashMap<>();
            res.put("emergency", false);
            res.put("reason", "no_rule_matched");
            return res;
        }

        // 2) 좌표 결정: 요청값 우선, 없으면 최신 위치
        double lat, lon;
        if (request.getLatitude() != null && request.getLongitude() != null) {
            lat = request.getLatitude();
            lon = request.getLongitude();
        } else {
            MemberLocation latest = memberLocationRepository.findByMemberEmail(reporterEmail)
                    .orElseThrow(() -> new IllegalStateException("최근 위치가 없습니다. 먼저 /api/location/me 로 업로드하세요."));
            lat = latest.getLatitude();
            lon = latest.getLongitude();
        }

        // 3) 이벤트 저장(감사/추적용)
        EmergencyEvent event = eventRepository.save(EmergencyEvent.builder()
                .reporterId(reporterEmail)
                .latitude(lat)
                .longitude(lon)
                .heartRate(request.getHeartRate())
                .emergencyType(type)
                .regionCode(null) // 필요 시 채우기
                .build());

        // 4) 반경(기본 1000m)
        int radius = Optional.ofNullable(request.getRadiusMeters()).orElse(1000);

        // 5) 반경 내 후보 조회(최대 300명)
        List<String> nearby = geo.nearby(lon, lat, radius, 300).stream()
                .filter(id -> !id.equalsIgnoreCase(reporterEmail)) // 신고자 제외
                .toList();

        // 6) HELPI만 필터
        List<Member> helpers = memberRepository.findByEmailInAndRole(nearby, MemberType.HELPI);
        List<String> helperIds = helpers.stream().map(Member::getEmail).toList();

        // 7) 알림 발송(비동기)
        int notified = notificationService.sendEmergencyAlert(
                helperIds,
                new EmergencyAlertPayLoad(
                        event.getId(),                      // Long
                        lat,                                // double
                        lon,                                // double
                        type,                               // String (e.g., "CARDIAC_ARREST" or "STROKE")
                        "[응급] 가까운 곳에서 도움이 필요합니다",  // title
                        type.equals("CARDIAC_ARREST")
                                ? "119 도착 전까지 가슴압박 30회-인공호흡 2회 반복. AED 위치 확인." // message
                                : "주변 안전 확보, 의식/호흡 확인 후 119 신고. 안내에 따르세요.",
                        List.of()                           // List<String> 이미지 URL (비워둠)
                )
        );

        // 8) 응답(네 스타일: Map)
        Map<String, Object> res = new HashMap<>();
        res.put("emergency", true);
        res.put("eventId", event.getId());
        res.put("radiusMeters", radius);
        res.put("notifiedCount", notified);
        res.put("center", Map.of("lat", lat, "lon", lon));
        res.put("type", type);
        return res;
    }
}
