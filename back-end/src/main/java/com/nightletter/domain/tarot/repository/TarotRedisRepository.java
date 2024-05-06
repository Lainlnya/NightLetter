package com.nightletter.domain.tarot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.nightletter.domain.tarot.entity.PastTarot;

@Repository
public interface TarotRedisRepository extends CrudRepository<PastTarot, Integer> {
}
