package com.nightletter.domain.diary.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.nightletter.domain.diary.entity.DiaryShared;
import com.nightletter.domain.tarot.entity.PastTarot;

@Repository
public interface DiaryRedisRepository extends CrudRepository<DiaryShared, Integer> {
}
