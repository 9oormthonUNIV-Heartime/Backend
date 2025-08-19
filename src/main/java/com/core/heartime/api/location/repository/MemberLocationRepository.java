package com.core.heartime.api.location.repository;

import com.core.heartime.api.location.entity.MemberLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberLocationRepository extends JpaRepository<MemberLocation, Long> {
    // 이메일 기준으로 위치 찾기
    Optional<MemberLocation> findTopByMemberEmailOrderByCreatedAtDesc(String memberEmail);

    // 존재 여부 체크용 (예: 처음 위치 저장 여부 확인 등)
    boolean existsByMemberEmail(String memberEmail);

    Optional<MemberLocation> findByMemberEmail(String memberEmail);
}
