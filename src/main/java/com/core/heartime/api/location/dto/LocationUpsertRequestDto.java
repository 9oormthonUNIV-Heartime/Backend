package com.core.heartime.api.location.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LocationUpsertRequestDto {

    @NotNull
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    private Double latitude;

    @NotNull
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    private Double longitude;

    @PositiveOrZero
    private Double accuracy;

    private Long clientTimestampMs;

    public LocationUpsertRequestDto(Double latitude, Double longitude, Double accuracy, Long clientTimestampMs) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.clientTimestampMs = clientTimestampMs;
    }
}
