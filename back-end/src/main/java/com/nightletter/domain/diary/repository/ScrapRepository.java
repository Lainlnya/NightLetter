package com.nightletter.domain.diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.entity.Scrap;
import com.nightletter.domain.member.entity.Member;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {
	long deleteByMemberAndDiary(Member member, Diary diary);
}
