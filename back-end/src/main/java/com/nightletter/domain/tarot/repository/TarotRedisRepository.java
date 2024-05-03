package com.nightletter.domain.tarot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.nightletter.domain.tarot.entity.PastTarot;
import com.nightletter.domain.tarot.entity.TodayTarot;

@Repository
public interface TarotRedisRepository extends CrudRepository<TodayTarot, Integer> {
}
