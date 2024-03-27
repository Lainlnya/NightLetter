package com.nightletter.domain.diary.repository;

import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.entity.QDiary;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class DiaryCustomRepositoryImpl implements DiaryCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Diary> findDiariesByMemberId(Integer memberId, LocalDate sttDate, LocalDate endDate) {
        QDiary diary = QDiary.diary;

        List<Diary> diaries = queryFactory.select(diary)
                .where(diary.writer.memberId.eq(memberId)
                        .and(diary.date.between(sttDate, endDate)))
                .fetch();

        return null;
    }
}
