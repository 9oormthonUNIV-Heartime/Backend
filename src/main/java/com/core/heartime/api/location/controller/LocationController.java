package com.core.heartime.api.location.controller;

import com.core.heartime.api.location.dto.LocationUpsertRequestDto;
import com.core.heartime.api.location.dto.NearbyRequestDto;
import com.core.heartime.api.location.service.LocationService;
import com.core.heartime.common.response.ApiResponse;
import com.core.heartime.common.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/location")
public class LocationController {

    private final LocationService locationService;

    @Operation(summary = "내 위치 업서트", description = "현재 위도/경도를 저장하고 Redis GEO 인덱스를 갱신합니다.")
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<Map<String, Object>>> upsertMyLocation(
            @AuthenticationPrincipal(expression = "username") String email,
            @Valid @RequestBody LocationUpsertRequestDto request
    ) {
        Map<String, Object> result = locationService.upsertMyLocation(email, request);
        return ApiResponse.success(SuccessStatus.LOCATION_UPSERT_SUCCESS, result);
    }

    @Operation(summary = "내 최신 위치 조회", description = "서버에 저장된 나의 최신 위치와 생성/수정 시각을 반환합니다.")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMyLocation(
            @AuthenticationPrincipal(expression = "username") String email
    ) {
        Map<String, Object> result = locationService.getMyLocation(email);
        return ApiResponse.success(SuccessStatus.LOCATION_ME_SUCCESS, result);
    }

    @Operation(summary = "실시간 공유 일시중지", description = "Redis GEO 인덱스에서 나를 제거하여 반경 검색 대상에서 제외합니다.")
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Map<String, Object>>> pauseSharing(
            @AuthenticationPrincipal(expression = "username") String email
    ) {
        Map<String, Object> result = locationService.pauseSharing(email);
        return ApiResponse.success(SuccessStatus.LOCATION_PAUSE_SUCCESS, result);
    }

    @Operation(summary = "주변 사용자 조회", description = "기준 좌표와 반경(미터)으로 주변 사용자 이메일 목록을 반환합니다.")
    @GetMapping("/nearby")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getNearby(
            @Valid @ModelAttribute NearbyRequestDto request
    ) {
        Map<String, Object> result = locationService.getNearbyMembers(request);
        return ApiResponse.success(SuccessStatus.LOCATION_NEARBY_SUCCESS, result);
    }
}
