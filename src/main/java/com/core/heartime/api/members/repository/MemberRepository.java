package com.core.heartime.api.members.repository;

import com.core.heartime.api.members.entity.Member;
import com.core.heartime.api.members.entity.MemberType;
import org.springframework.context.annotation.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickname(String nickname);
    Optional<Member> findByPhoneNumber(String phoneNumber);
    List<Member> findByEmailInAndRole(Collection<String> emails, MemberType role);
}
