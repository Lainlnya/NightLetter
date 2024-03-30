package com.nightletter.domain.diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nightletter.domain.diary.entity.DiaryTarot;

public interface DiaryTarotRepository extends JpaRepository<DiaryTarot, Long> {
}
