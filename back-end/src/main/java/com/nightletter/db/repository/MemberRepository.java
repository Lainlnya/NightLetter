package com.nightletter.db.repository;

import com.nightletter.db.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    Member findMemberById(Integer id);
    Member findMemberByOAuth2Id(String OAuth2Id);
}

