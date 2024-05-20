package com.nightletter.domain.tarot.repository;

import com.nightletter.domain.tarot.entity.FutureTarot;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TarotFutureRedisRepository extends CrudRepository<FutureTarot, Integer> {
}
