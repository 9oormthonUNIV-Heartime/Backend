package com.core.heartime.api.location.service;

import com.core.heartime.api.location.geo.RedisGeoService;
import com.core.heartime.api.location.repository.MemberLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final MemberLocationRepository memberLocationRepository;
    private RedisGeoService redisGeoService;
}
