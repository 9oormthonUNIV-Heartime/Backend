package com.core.heartime.api.location.geo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Redis GEO에 쌓인 오래된 좌표를 주기적으로 청소.
 * - 예: 최근 15분 이상 업데이트 없는 사용자 제거
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GeoPurgeJob {

    private final RedisGeoService geo;

    // 5분마다 실행 (fixedDelay = 이전 실행 종료 후 5분 뒤)
    @Scheduled(fixedDelayString = "PT5M")
    public void purge() {
        long removed = geo.purgeStale(Duration.ofMinutes(15));
        if (removed > 0) {
            log.info("Redis GEO purge removed {} stale members", removed);
        }
    }
}
