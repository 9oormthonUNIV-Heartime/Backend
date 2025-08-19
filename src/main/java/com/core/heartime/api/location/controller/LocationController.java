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
RequiredArgsConstructor
@RequestMapping("/api/v1/location")
public class LocationController {
    private final LocationService locationService;

    @Operation(
            summary = "내 위치 업데이트",
            description = "사용자의 현재 위치(위도/경도/정확도)를 서버에 저장하고, 실시간 GEO 인덱스를 갱신합니다."
    )
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<Map<String, Object>>> upsertMyLocation(
            @AuthenticationPrincipal(expression = "username") String email,
            @Valid @RequestBody LocationUpsertRequestDto request
    ) {
        Map<String, Object> result = locationService.upsertMyLocation(email, request);
        return ApiResponse.success(SuccessStatus.LOCATION_UPSERT_SUCCESS, result);

    }

    @Operation(
            summary = "주변 사용자 조회",
            description = "기준 좌표와 반경(미터)으로 주변 사용자 아이디 목록을 반환합니다."
    )
    @GetMapping("/nearby")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getNearby(
            @Valid @ModelAttribute NearbyRequestDto request
    ) {
        Map<String, Object> result = locationService.getNearbyMembers(request);
        return ApiResponse.success(SuccessStatus.LOCATION_NEARBY_SUCCESS, result);
    }

};
