package com.nightletter.domain.diary.repository;

import com.nightletter.domain.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import java.util.List;
import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.entity.QDiary;

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
}
