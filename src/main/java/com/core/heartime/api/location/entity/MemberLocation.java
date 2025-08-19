package com.core.heartime.api.location.entity;

import com.core.heartime.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
@Table(name = "member_location")
public class MemberLocation extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이메일을 기준으로 위치를 구분 (회원 식별자로 email 사용 중)
    @Column(name = "member_email", nullable = false, unique = true, length = 50)
    private String memberEmail;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    // 위치 정확도 (미터 단위), 선택 값
    @Column(name = "accuracy")
    private Double accuracy;

    /**
     * 위치 업데이트용 메서드
     */
    public void update(Double latitude, Double longitude, Double accuracy) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
    }


}
