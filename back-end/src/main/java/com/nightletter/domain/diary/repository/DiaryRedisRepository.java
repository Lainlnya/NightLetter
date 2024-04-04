package com.nightletter.domain.diary.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.nightletter.domain.diary.entity.DiaryShared;

@Repository
public interface DiaryRedisRepository extends CrudRepository<DiaryShared, Integer> {
}
