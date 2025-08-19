package com.core.heartime.api.location.geo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Redis GEO 기반 반경검색 유틸.
 * 좌표 순서 주의: Redis는 (경도, 위도) = (lon, lat) 입니다.
 */
@Service
@RequiredArgsConstructor
public class RedisGeoService {

    private final StringRedisTemplate redis;

    /**
     * 좌표 upsert (경도, 위도 순서!)
     * - GEOSET에 저장
     * - 타임스탬프 HASH에 기록
     */
    public void upsert(String memberId, double lon, double lat) {
        redis.opsForGeo().add(
                GeoConstants.KEY_GEO,
                new RedisGeoCommands.GeoLocation<>(memberId, new Point(lon, lat))
        );
        redis.opsForHash().put(GeoConstants.KEY_TS, memberId, String.valueOf(Instant.now().toEpochMilli()));
    }

    /**
     * GEOSET에서 멤버 제거 (공유 일시중지 등)
     */
    public void remove(String memberId) {
        redis.opsForGeo().remove(GeoConstants.KEY_GEO, memberId);
        redis.opsForHash().delete(GeoConstants.KEY_TS, memberId);
    }

    /**
     * 반경 내 멤버 ID를 거리 오름차순으로 최대 limit명 반환
     * - 좌표는 (경도, 위도) 순서로 전달해야 함
     */
    public List<String> nearby(double lon, double lat, int radiusMeters, int limit) {
        var args = RedisGeoCommands.GeoRadiusCommandArgs
                .newGeoRadiusArgs()
                .includeDistance()
                .sortAscending()
                .limit(limit);

        var circle = new Circle(new Point(lon, lat), new Distance(radiusMeters));
        var results = redis.opsForGeo().radius(GeoConstants.KEY_GEO, circle, args);

        if (results == null || results.getContent().isEmpty()) return List.of();

        return results.getContent().stream()
                .map(r -> r.getContent().getName()) // memberId
                .collect(Collectors.toList());
    }

    /**
     * 반경 내 멤버 ID를 반환하되, 최근 업데이트가 maxAge 이내인 사용자만 포함.
     * - stale(오래된) 좌표를 결과에서 제거하고 싶을 때 사용
     */
    public List<String> nearbyWithTtl(double lon, double lat, int radiusMeters, int limit, Duration maxAge) {
        long now = System.currentTimeMillis();
        List<String> ids = nearby(lon, lat, radiusMeters, limit);
        if (ids.isEmpty()) return ids;

        Map<Object, Object> tsMap = redis.opsForHash().entries(GeoConstants.KEY_TS); // 간단 구현: 전체 읽기
        return ids.stream()
                .filter(id -> {
                    Object tsObj = tsMap.get(id);
                    if (tsObj == null) return false;
                    long ts = parseLongOrZero(tsObj.toString());
                    return (now - ts) <= maxAge.toMillis();
                })
                .collect(Collectors.toList());
    }

    /**
     * 오래된 엔트리 정리(청소 작업): maxAge 를 초과한 사용자들을 GEO/HASH에서 제거
     * - 운영에서는 스케줄러(@Scheduled)로 주기 실행 권장
     *
     * @return 제거된 사용자 수
     */
    public long purgeStale(Duration maxAge) {
        long now = System.currentTimeMillis();
        Map<Object, Object> tsMap = redis.opsForHash().entries(GeoConstants.KEY_TS);
        if (tsMap.isEmpty()) return 0;

        List<String> toRemove = new ArrayList<>();
        for (Map.Entry<Object, Object> e : tsMap.entrySet()) {
            String memberId = String.valueOf(e.getKey());
            long ts = parseLongOrZero(String.valueOf(e.getValue()));
            if ((now - ts) > maxAge.toMillis()) {
                toRemove.add(memberId);
            }
        }

        if (!toRemove.isEmpty()) {
            // GEOSET에서 일괄 제거
            redis.opsForGeo().remove(GeoConstants.KEY_GEO, toRemove.toArray());
            // HASH에서도 제거
            redis.opsForHash().delete(GeoConstants.KEY_TS, toRemove.toArray());
        }
        return toRemove.size();
    }

    private long parseLongOrZero(String s) {
        try { return Long.parseLong(s); } catch (Exception ignore) { return 0L; }
    }
}
