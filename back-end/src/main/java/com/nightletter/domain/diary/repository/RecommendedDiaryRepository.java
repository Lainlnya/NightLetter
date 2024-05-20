package com.nightletter.domain.diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nightletter.domain.diary.entity.RecommendedDiary;

public interface RecommendedDiaryRepository extends JpaRepository<RecommendedDiary, Long> {

}
