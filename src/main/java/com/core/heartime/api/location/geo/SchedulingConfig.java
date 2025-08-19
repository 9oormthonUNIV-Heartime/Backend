package com.core.heartime.api.location.geo;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @EnableScheduling 활성화.
 * 프로젝트 전역에 이미 스케줄링을 켜는 설정이 있으면 이 파일은 생략 가능.
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {}
