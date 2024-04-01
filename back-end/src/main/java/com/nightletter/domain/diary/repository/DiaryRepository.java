package com.nightletter.domain.diary.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.member.entity.Member;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long>, DiaryCustomRepository {

	List<Diary> findDiariesByWriter(Member writer);

	Diary findDiaryByDiaryId(Long diaryId);

	Optional<Diary> findByWriterMemberIdAndDate(Integer writer_memberId, LocalDate date);

}
