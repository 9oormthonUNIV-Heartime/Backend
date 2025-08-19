package com.core.heartime.api.location.service;

import com.core.heartime.api.location.dto.LocationUpsertRequestDto;
import com.core.heartime.api.location.dto.NearbyRequestDto;
import com.core.heartime.api.location.entity.MemberLocation;
import com.core.heartime.api.location.geo.RedisGeoService;
import com.core.heartime.api.location.repository.MemberLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final MemberLocationRepository memberLocationRepository;
    private final RedisGeoService redisGeoService;

    @Transactional
    public Map<String, Object> upsertMyLocation(String email, LocationUpsertRequestDto request) {

        MemberLocation entity = memberLocationRepository.findByMemberEmail(email)
                .map(e -> { e.update(request.getLatitude(), request.getLongitude(), request.getAccuracy()); return e; })
                .orElseGet(() -> MemberLocation.builder()
                        .memberEmail(email)
                        .latitude(request.getLatitude())
                        .longitude(request.getLongitude())
                        .accuracy(request.getAccuracy())
                        .build());

        memberLocationRepository.save(entity);

        // ⚠️ Redis GEO 는 (경도, 위도) 순서!
        redisGeoService.upsert(email, request.getLongitude(), request.getLatitude());

        Map<String, Object> res = new HashMap<>();
        res.put("memberEmail", email);
        res.put("latitude", entity.getLatitude());
        res.put("longitude", entity.getLongitude());
        res.put("accuracy", entity.getAccuracy());
        res.put("updatedAt", Instant.now());
        return res;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getNearbyMembers(NearbyRequestDto request) {
        List<String> memberEmails = redisGeoService.nearby(
                request.getLongitude(), request.getLatitude(), request.getRadiusMeters(), request.getLimit());

        Map<String, Object> res = new HashMap<>();
        res.put("count", memberEmails.size());
        res.put("memberEmails", memberEmails);
        res.put("center", Map.of("lat", request.getLatitude(), "lon", request.getLongitude()));
        res.put("radiusMeters", request.getRadiusMeters());
        return res;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getMyLocation(String email) {
        MemberLocation ml = memberLocationRepository.findByMemberEmail(email)
                .orElseThrow(() -> new IllegalStateException("최근 위치가 없습니다. 먼저 /api/location/me 로 업로드하세요."));

        Map<String, Object> res = new HashMap<>();
        res.put("memberEmail", email);
        res.put("latitude", ml.getLatitude());
        res.put("longitude", ml.getLongitude());
        res.put("accuracy", ml.getAccuracy());
        res.put("createdAt", ml.getCreatedAt());
        res.put("updatedAt", ml.getUpdatedAt());
        return res;
    }

    @Transactional
    public Map<String, Object> pauseSharing(String email) {
        redisGeoService.remove(email);
        Map<String, Object> res = new HashMap<>();
        res.put("memberEmail", email);
        res.put("sharing", "paused");
        res.put("timestamp", Instant.now());
        return res;
    }
}
