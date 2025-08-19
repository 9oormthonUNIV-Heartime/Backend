package com.core.heartime.api.location.geo;

/**
 * Redis GEO/보조 Hash 키 등 상수 모음
 */
public final class GeoConstants {

    private GeoConstants() {}

    /** 최신 좌표를 보관하는 GEOSET 키 */
    public static final String KEY_GEO = "geo:member:latest";
    /** 마지막 업데이트 시각(ms) 보관용 HASH 키 */
    public static final String KEY_TS  = "geo:member:ts";

    /** 기본 반경(미터) = 1km */
    public static final int DEFAULT_RADIUS_METERS = 1000;
    /** 인근 조회 시 최대 결과 수 = 주변 인원 수*/
    public static final int DEFAULT_NEARBY_LIMIT = 300;

}
