package com.nightletter.domain.diary.repository;

import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.entity.DiaryType;
import com.nightletter.domain.diary.entity.QDiary;
import com.nightletter.domain.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.entity.QDiary;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DiaryCustomRepositoryImpl implements DiaryCustomRepository {

	private final JPAQueryFactory queryFactory;

    @Override
    public List<Diary> findDiariesByMember(Member member, LocalDate sttDate, LocalDate endDate) {
        QDiary diary = QDiary.diary;

        List<Diary> diaries = queryFactory.select(diary)
                .from(diary)
                .where(diary.writer.eq(member)
                        .and(diary.date.between(sttDate, endDate)))
                .fetch();

        return diaries;
    }


    @Transactional
//    @Modifying
    @Override
    public long updateDiaryType(@Param("diary_id") Long diaryId, @Param("diary_type")DiaryType diaryType) {
        QDiary diary = QDiary.diary;

        System.out.println("in REPO");
        System.out.println(diaryId);
        System.out.println(diaryType);

        return queryFactory.update(diary)
                .set(diary.type, diaryType)
                .where(diary.diaryId.eq(diaryId))
                .execute();
    }
}
