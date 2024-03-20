package com.example.authservice.db.repository;

import com.example.authservice.db.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    Member findByMemberId(Integer memberId);
    Member findMemberByOAuth2Id(String OAuth2Id);
}

