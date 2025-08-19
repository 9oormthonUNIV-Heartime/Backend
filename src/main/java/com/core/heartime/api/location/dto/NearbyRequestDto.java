package com.core.heartime.api.location.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NearbyRequestDto {

    @NotNull
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    private Double latitude;

    @NotNull
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    private Double longitude;

    @Positive
    @Max(20000) // 최대 20km
    private Integer radiusMeters;

    @Positive
    @Max(300)   // 최대 300명
    private Integer limit;

    public NearbyRequestDto(Double latitude, Double longitude, Integer radiusMeters, Integer limit) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.radiusMeters = radiusMeters;
        this.limit = limit;
    }
}
