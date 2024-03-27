package com.nightletter.domain.diary.repository;

import com.nightletter.domain.diary.dto.DiaryListRequest;
import com.nightletter.domain.diary.dto.DiaryRequestDirection;
import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.entity.QDiary;
import com.nightletter.domain.diary.entity.QDiaryTarot;
import com.nightletter.domain.diary.entity.QTarot;
import com.nightletter.domain.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class DiaryCustomRepositoryImpl implements DiaryCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Diary> findDiariesByMemberId(Long memberId, LocalDate sttDate, LocalDate endDate) {
        QDiary diary = QDiary.diary;

        List<Diary> diaries = queryFactory.select(diary)
                .where(diary.writer.id.eq(memberId)
                        .and(diary.date.between(sttDate, endDate)))
                .fetch();

        return diaries;
    }
}
