package com.nightletter.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nightletter.domain.member.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer>, MemberCustomRepository {

	Member findMemberByOAuth2Id(String OAuth2Id);

	Member findByMemberId(Integer currentMemberId);
}

