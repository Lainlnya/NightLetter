package com.nightletter.domain.diary.repository;

import java.util.List;

import com.nightletter.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nightletter.domain.diary.entity.Diary;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Integer>, DiaryCustomRepository {

    List<Diary> findDiariesByWriter(Member writer);
}
